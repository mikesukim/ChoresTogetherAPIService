package chorestogetherapiservice.activity

import chorestogetherapiservice.domain.JsonResponse
import chorestogetherapiservice.domain.User
import chorestogetherapiservice.domain.UserEmail
import chorestogetherapiservice.exception.DependencyFailureInternalException
import chorestogetherapiservice.exception.activity.DependencyFailureException
import chorestogetherapiservice.exception.activity.ResourceNotFoundException
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

    def jsonResponseMock = Mock(JsonResponse.class)

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
        def rawUserEmail = "testUserEmail"
        def expectedResult = Response.status(200).entity(jsonResponseMock).build()

        when:
        def result = getUserActivity.getUser(rawUserEmail)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * getUserLogicMock.getUser(_ as UserEmail) >> new User(rawUserEmail)
        1 * responseHandlerMock.generateSuccessResponse() >> expectedResult
        0 * _
    }

    def 'test when DependencyFailureInternalException raised'() {
        given:
        def rawUserEmail = "testUserEmail"

        when:
        getUserActivity.getUser(rawUserEmail)

        then:
        thrown(DependencyFailureException)

        1 * getUserLogicMock.getUser( _ as UserEmail) >> {throw new DependencyFailureInternalException("", new Exception())}
        0 * _
    }

    def 'test when NoItemFoundException raised'() {
        given:
        def rawUserEmail = "testUserEmail"

        when:
        getUserActivity.getUser(rawUserEmail)

        then:
        thrown(ResourceNotFoundException)

        1 * getUserLogicMock.getUser( _ as UserEmail) >> {throw new NoItemFoundException("")}
        0 * _
    }

}
