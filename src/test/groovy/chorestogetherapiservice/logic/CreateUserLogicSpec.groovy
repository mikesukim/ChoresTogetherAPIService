package chorestogetherapiservice.logic

import chorestogetherapiservice.datastore.UserDao
import chorestogetherapiservice.domain.ImmutableUser
import chorestogetherapiservice.domain.ResponseEntity
import chorestogetherapiservice.exception.datastore.ItemAlreadyExistException
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException
import chorestogetherapiservice.handler.JwtHandler
import chorestogetherapiservice.handler.ResponseHandler
import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.core.Response

class CreateUserLogicSpec extends Specification {

    UserDao userDaoMock = Mock(UserDao.class)

    ResponseHandler responseHandlerMock = Mock(ResponseHandler.class)

    JwtHandler jwtHandlerMock = Mock(JwtHandler.class)

    ResponseEntity responseEntityMock = Mock(ResponseEntity.class)

    @Subject
    CreateUserLogic createUserLogic = new CreateUserLogic(userDaoMock, responseHandlerMock, jwtHandlerMock)

    def "test success createUser"() {
        given:
        def user = ImmutableUser.builder().email("fake@gmail.com").build()
        def tokenMock = "tokenMock"
        def dataMock = new HashMap<String, String>()
        dataMock.put("token", tokenMock)
        def expectedResult = Response.status(200).entity(responseEntityMock).build()

        when:
        def result = createUserLogic.createUser(user)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * userDaoMock.create(user)
        1 * jwtHandlerMock.generateJwt(user) >> tokenMock
        1 * responseHandlerMock.generateSuccessResponseWith(dataMock) >> expectedResult
        0 * _
    }

    def "test when ItemAlreadyExistException raise"() {
        given:
        def user = ImmutableUser.builder().email("fake@gmail.com").build()
        def expectedResult = Response.status(400).entity(responseEntityMock).build()

        when:
        def result = createUserLogic.createUser(user)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * userDaoMock.create(user) >> {throw new ItemAlreadyExistException(_ as String)}
        1 * responseHandlerMock.generateBadRequestErrorResponseWith(_) >> expectedResult
        0 * _
    }

    def "test when dependencyException raise"() {
        given:
        def user = ImmutableUser.builder().email("fake@gmail.com").build()
        def message = "error message"
        def cause = new Exception()
        def expectedResult = Response.status(400).entity(responseEntityMock).build()

        when:
        def result = createUserLogic.createUser(user)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * userDaoMock.create(user) >> {throw new DependencyFailureInternalException(message,cause)}
        1 * responseHandlerMock.generateFailResponseWith(_) >> expectedResult
        0 * _
    }
}
