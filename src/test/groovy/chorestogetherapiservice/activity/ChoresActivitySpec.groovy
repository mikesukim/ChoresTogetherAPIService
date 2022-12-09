package chorestogetherapiservice.activity

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import javax.validation.Validation
import javax.validation.ValidatorFactory
import javax.validation.executable.ExecutableValidator

class ChoresActivitySpec extends Specification {

    @Subject
    ChoresActivity choresActivity = new ChoresActivity()

    //TODO: import Spock.guice to inject Hibernate from Guice and remove validator initialization at setup()
    ExecutableValidator validator

    def setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory()
        validator = factory.getValidator().forExecutables()
    }

    def 'test getChore success with valid rawChoreId'() {
        when:
        def result = choresActivity.getChore("testRawChoreId")

        then:
        result == 'hello chore'
    }

    @Unroll
    def 'test getChore validation check fail with null/empty rawChoreId  '() {
        when:
        // Hibernate Doc for how to test method validation
        // https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-validating-executable-constraints
        def violations = validator.validateParameters(
                choresActivity,
                ChoresActivity.class.getMethod("getChore", String.class),
                [rawChoreId].toArray()
        )

        then:
        violations.size() != 0

        where:
        rawChoreId << [null, ""]
    }

    def 'test createChore'() {
        when:
        def result = choresActivity.createChore()

        then:
        result == 'hello chore'
    }
}
