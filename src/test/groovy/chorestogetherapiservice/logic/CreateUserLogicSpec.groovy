package chorestogetherapiservice.logic

import chorestogetherapiservice.TestingConstant
import chorestogetherapiservice.datastore.UserDao
import chorestogetherapiservice.domain.ImmutableToken
import chorestogetherapiservice.domain.ResponseEntity
import chorestogetherapiservice.domain.Token
import chorestogetherapiservice.exception.datastore.ItemAlreadyExistException
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException
import chorestogetherapiservice.handler.JwtHandler
import chorestogetherapiservice.handler.ResponseHandler
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.core.Response

class CreateUserLogicSpec extends Specification {

    UserDao userDaoMock = Mock(UserDao.class)

    ResponseHandler responseHandlerMock = Mock(ResponseHandler.class)

    JwtHandler jwtHandlerMock = Mock(JwtHandler.class)

    ResponseEntity responseEntityMock = Mock(ResponseEntity.class)

    @Shared
    def user = TestingConstant.USER

    @Subject
    CreateUserLogic createUserLogic = new CreateUserLogic(userDaoMock, responseHandlerMock, jwtHandlerMock)

    def "test success createUser"() {
        given:
        def rawTokenMock = "tokenMock"
        def tokenMock = ImmutableToken.builder().token(rawTokenMock).build()
        def expectedResult = Response.status(200).entity(responseEntityMock).build()

        when:
        def result = createUserLogic.createUser(user)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * jwtHandlerMock.generateJwt(user) >> rawTokenMock
        1 * userDaoMock.create(user,{it.getToken() == rawTokenMock} as Token)
        1 * responseHandlerMock.generateSuccessResponseWith(tokenMock) >> expectedResult
        0 * _
    }

    def "test when ItemAlreadyExistException raise"() {
        given:
        def rawTokenMock = "tokenMock"
        def expectedResult = Response.status(400).entity(responseEntityMock).build()

        when:
        def result = createUserLogic.createUser(user)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * jwtHandlerMock.generateJwt(user) >> rawTokenMock
        1 * userDaoMock.create(user,{it.getToken() == rawTokenMock} as Token) >> {throw new ItemAlreadyExistException(_ as String)}
        1 * responseHandlerMock.generateBadRequestErrorResponseWith(_) >> expectedResult
        0 * _
    }

    def "test when dependencyException raise"() {
        given:
        def rawTokenMock = "tokenMock"
        def message = "error message"
        def expectedResult = Response.status(400).entity(responseEntityMock).build()

        when:
        def result = createUserLogic.createUser(user)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * jwtHandlerMock.generateJwt(user) >> rawTokenMock
        1 * userDaoMock.create(user,{it.getToken() == rawTokenMock} as Token) >> {throw new DependencyFailureInternalException(message)}
        1 * responseHandlerMock.generateFailResponseWith(_) >> expectedResult
        0 * _
    }
}
