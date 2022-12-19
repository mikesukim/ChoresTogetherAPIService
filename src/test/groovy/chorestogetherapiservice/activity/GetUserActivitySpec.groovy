package chorestogetherapiservice.activity

import chorestogetherapiservice.logic.GetUserLogic
import chorestogetherapiservice.logic.GetUserLogicSpec
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.ValidatorFactory
import javax.validation.executable.ExecutableValidator
import javax.ws.rs.core.Response

class GetUserActivitySpec extends Specification {

    GetUserLogic getUserLogicMock = Mock(GetUserLogic.class)

    //TODO: import Spock.guice to inject Hibernate from Guice and remove validator initialization at setup()
    ExecutableValidator validator

    @Subject
    GetUserActivity getUserActivity = new GetUserActivity(getUserLogicMock)

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator().forExecutables()
    }

    def 'test getUser success with valid userEmailInput'() {
        when:
        def userEmailMock = "testUserEmail"
        def result = getUserActivity.getUser(userEmailMock)
        def expectedResult = Response.status(200).entity("getUser is called, email : " + userEmailMock).build()

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity
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


}
