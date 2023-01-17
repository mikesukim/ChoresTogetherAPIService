package chorestogetherapiservice.activity

import chorestogetherapiservice.domain.ImmutableUser
import chorestogetherapiservice.domain.ResponseEntity
import chorestogetherapiservice.domain.UserEmail
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException
import chorestogetherapiservice.exception.datastore.NoItemFoundException
import chorestogetherapiservice.handler.ResponseHandler
import chorestogetherapiservice.logic.GetUserLogic
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.ValidatorFactory
import javax.validation.executable.ExecutableValidator
import javax.ws.rs.core.Response

class GetUserActivitySpec extends Specification {

    def getUserLogicMock = Mock(GetUserLogic.class)

    def responseHandlerMock = Mock(ResponseHandler.class)

    def responseEntityMock = Mock(ResponseEntity.class)

    //TODO: import Spock.guice to inject Hibernate from Guice and remove validator initialization at setup()
    ExecutableValidator validator

    @Subject
    GetUserActivity getUserActivity = new GetUserActivity(getUserLogicMock, responseHandlerMock)

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator().forExecutables()
    }

    @Unroll
    def 'test getUser validation check fail with null/empty userEmailInput'() {
        when:
        // Hibernate Doc for how to test method validation
        // https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-validating-executable-constraints
        def violations = validator.validateParameters(
                getUserActivity,
                GetUserActivity.class.getMethod("getUser", String.class),
                [userEmailInput].toArray()
        )

        then:
        violations.size() != 0

        where:
        userEmailInput << [null, ""]
    }

    def 'test getUser success with valid userEmailInput'() {
        given:
        def rawUserEmail = "testUserEmail@email.com"
        def expectedResult = Response.status(200).entity(responseEntityMock).build()

        when:
        def result = getUserActivity.getUser(rawUserEmail)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * getUserLogicMock.getUser(_ as UserEmail) >> ImmutableUser.builder().email("fake@gmail.com").build()
        1 * responseHandlerMock.generateSuccessResponse() >> expectedResult
        0 * _
    }

    def 'test when DependencyFailureInternalException raised'() {
        given:
        def rawUserEmail = "testUserEmail@gmail.com"
        def expectedResult = Response.status(500).entity(responseEntityMock).build()

        when:
        def result = getUserActivity.getUser(rawUserEmail)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * getUserLogicMock.getUser( _ as UserEmail) >> {throw new DependencyFailureInternalException("", new Exception())}
        1 * responseHandlerMock.generateFailResponseWith(_) >> expectedResult
        0 * _
    }

    def 'test when NoItemFoundException raised'() {
        given:
        def rawUserEmail = "testUserEmail@gmail.com"
        def expectedResult = Response.status(400).entity(responseEntityMock).build()

        when:
        def result = getUserActivity.getUser(rawUserEmail)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * getUserLogicMock.getUser( _ as UserEmail) >> {throw new NoItemFoundException("")}
        1 * responseHandlerMock.generateResourceNotFoundErrorResponseWith(_) >> expectedResult
        0 * _
    }

}
