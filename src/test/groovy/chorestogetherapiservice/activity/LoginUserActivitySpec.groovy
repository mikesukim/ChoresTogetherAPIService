package chorestogetherapiservice.activity

import chorestogetherapiservice.domain.ImmutableSocialIdToken
import chorestogetherapiservice.domain.SocialIdToken
import chorestogetherapiservice.handler.ResponseHandler
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.ValidatorFactory
import javax.validation.executable.ExecutableValidator
import javax.ws.rs.core.Response

class LoginUserActivitySpec extends Specification {

    def responseHandlerMock = Mock(ResponseHandler.class)

    //TODO: import Spock.guice to inject Hibernate from Guice and remove validator initialization at setup()
    ExecutableValidator validator

    @Subject
    def loginUserActivity = new LoginUserActivity(responseHandlerMock)

    @Shared
    def socialIdTokenMock =  ImmutableSocialIdToken.builder().token("rawToken").build()

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator().forExecutables()
    }

    @Unroll
    def 'test loginUser validation check fail when null/empty token is passed'() {
        when:
        // Hibernate Doc for how to test method validation
        // https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-validating-executable-constraints
        def violations = validator.validateParameters(
                loginUserActivity,
                LoginUserActivity.class.getMethod("googleLogin", SocialIdToken.class),
                [tokenInput].toArray()
        )

        then:
        violations.size() == expectedViolationSize

        where:
        tokenInput            | expectedViolationSize
        null                  | 1
        socialIdTokenMock     | 0
    }

    def 'test google login'() {
        given:
        def expectedResult = Response.status(500).entity(null).build()

        when:
        def result = loginUserActivity.googleLogin(socialIdTokenMock)

        then:
        result == expectedResult

        1 * responseHandlerMock.generateFailResponseWith(_) >> expectedResult
        0 * _
    }

    def 'test Kakao login'() {
        given:
        def expectedResult = Response.status(500).entity(null).build()

        when:
        def result = loginUserActivity.kakaoTalkLogin(socialIdTokenMock)

        then:
        result == expectedResult

        1 * responseHandlerMock.generateFailResponseWith(_) >> expectedResult
        0 * _
    }
}
