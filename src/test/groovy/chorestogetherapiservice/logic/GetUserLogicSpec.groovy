package chorestogetherapiservice.logic

import chorestogetherapiservice.datastore.UserDao
import chorestogetherapiservice.datastore.UserItem
import chorestogetherapiservice.datastore.UserItemBuilder
import chorestogetherapiservice.datastore.UserItemSpec
import chorestogetherapiservice.domain.ImmutableUser
import chorestogetherapiservice.domain.ImmutableUserEmail
import chorestogetherapiservice.domain.ResponseEntity
import chorestogetherapiservice.domain.User
import chorestogetherapiservice.domain.UserEmail
import chorestogetherapiservice.exception.datastore.NoItemFoundException
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException
import chorestogetherapiservice.handler.ResponseHandler
import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.core.Response
import java.time.Instant

class GetUserLogicSpec extends Specification {

    UserDao userDaoMock = Mock(UserDao.class)

    def responseHandlerMock = Mock(ResponseHandler.class)

    def responseEntityMock = Mock(ResponseEntity.class)

    @Subject
    GetUserLogic getUserLogic = new GetUserLogic(userDaoMock, responseHandlerMock)

    def "test success getUser"() {
        given:
        def userEmail = ImmutableUserEmail.builder().email("fake@gmail.com").build()
        def userItem = new UserItemBuilder().email("fake@gmail.com").registrationDate(Instant.now()).token("rawToken").build()
        def expectedResult = Response.status(200).entity(responseEntityMock).build()

        when:
        def result = getUserLogic.getUser(userEmail)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * userDaoMock.get(userEmail) >> userItem
        1 * responseHandlerMock.generateSuccessResponseWith(_ as User) >> expectedResult
        0 * _
    }

    def "test raising NoItemFoundException when no user was found"() {
        given:
        def userEmail = ImmutableUserEmail.builder().email("fake@gmail.com").build()
        def expectedResult = Response.status(404).entity(responseEntityMock).build()

        when:
        def result = getUserLogic.getUser(userEmail)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * userDaoMock.get(userEmail) >> {throw new NoItemFoundException()}
        1 * responseHandlerMock.generateResourceNotFoundErrorResponseWith(_) >> expectedResult
        0 * _
    }

    def "test when DependencyFailureInternalException raised"() {
        given:
        def userEmail = ImmutableUserEmail.builder().email("fake@gmail.com").build()
        def expectedResult = Response.status(500).entity(responseEntityMock).build()

        when:
        def result = getUserLogic.getUser(userEmail)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * userDaoMock.get(userEmail) >> {throw new DependencyFailureInternalException("", new Exception())}
        1 * responseHandlerMock.generateFailResponseWith(_) >> expectedResult
        0 * _
    }
}
