package chorestogetherapiservice.activity

import chorestogetherapiservice.domain.ImmutableUser
import chorestogetherapiservice.domain.ResponseEntity
import chorestogetherapiservice.domain.User
import chorestogetherapiservice.domain.UserEmail
import chorestogetherapiservice.handler.ResponseHandler
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.ValidatorFactory
import javax.validation.executable.ExecutableValidator
import javax.ws.rs.core.Response

class CreateUserActivitySpec extends Specification {

    def responseHandlerMock = Mock(ResponseHandler.class)

    def responseEntityMock = Mock(ResponseEntity.class)

    @Shared
    def userMock =  ImmutableUser.builder().email("aaa@aaa.com").build()

    //TODO: import Spock.guice to inject Hibernate from Guice and remove validator initialization at setup()
    ExecutableValidator validator

    @Subject
    CreateUserActivity createUserActivity = new CreateUserActivity(responseHandlerMock)

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
                createUserActivity,
                CreateUserActivity.class.getMethod("createUser", User.class),
                [userInput].toArray()
        )
        then:
        violations.size() == expectedViolationSize

        where:
        userInput    | expectedViolationSize
        null         | 1
        userMock     | 0
    }

    def 'test createUser success with valid User Input'() {
        given:
        def expectedResult = Response.status(400).entity(responseEntityMock).build()

        when:
        def result = createUserActivity.createUser(userMock)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * responseHandlerMock.generateFailResponseWith(_) >> expectedResult
        0 * _
    }


}
