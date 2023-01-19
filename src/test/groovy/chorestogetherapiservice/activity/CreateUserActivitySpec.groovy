package chorestogetherapiservice.activity

import chorestogetherapiservice.domain.ImmutableUser
import chorestogetherapiservice.domain.ResponseEntity
import chorestogetherapiservice.domain.User
import chorestogetherapiservice.domain.UserEmail
import chorestogetherapiservice.exception.datastore.ItemAlreadyExistException
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException
import chorestogetherapiservice.handler.ResponseHandler
import chorestogetherapiservice.logic.CreateUserLogic
import chorestogetherapiservice.logic.GetUserLogic
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.ValidatorFactory
import javax.validation.executable.ExecutableValidator
import javax.ws.rs.core.Response

class CreateUserActivitySpec extends Specification {

    def createUserLogicMock = Mock(CreateUserLogic.class)

    def responseHandlerMock = Mock(ResponseHandler.class)

    def responseEntityMock = Mock(ResponseEntity.class)

    @Shared
    def userMock =  ImmutableUser.builder().email("aaa@aaa.com").build()

    //TODO: import Spock.guice to inject Hibernate from Guice and remove validator initialization at setup()
    ExecutableValidator validator

    @Subject
    CreateUserActivity createUserActivity = new CreateUserActivity(responseHandlerMock, createUserLogicMock)

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator().forExecutables()
    }

    @Unroll
    def 'test createUser validation check'() {

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
        def expectedResult = Response.status(200).entity(responseEntityMock).build()

        when:
        def result = createUserActivity.createUser(userMock)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * createUserLogicMock.createUser(userMock)
        1 * responseHandlerMock.generateSuccessResponse() >> expectedResult
        0 * _
    }

    def 'test when DependencyFailureInternalException raised'() {
        given:
        def expectedResult = Response.status(500).entity(responseEntityMock).build()

        when:
        def result = createUserActivity.createUser(userMock)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * createUserLogicMock.createUser(userMock) >> {throw new DependencyFailureInternalException(_ as String, new Exception())}
        1 * responseHandlerMock.generateFailResponseWith(_) >> expectedResult
        0 * _
    }


    def 'test when ItemAlreadyExistException raised'() {
        given:
        def expectedResult = Response.status(400).entity(responseEntityMock).build()

        when:
        def result = createUserActivity.createUser(userMock)

        then:
        result.status == expectedResult.status
        result.entity == expectedResult.entity

        1 * createUserLogicMock.createUser(userMock) >> {throw new ItemAlreadyExistException(_ as String)}
        1 * responseHandlerMock.generateBadRequestErrorResponseWith(_) >> expectedResult
        0 * _
    }



}
