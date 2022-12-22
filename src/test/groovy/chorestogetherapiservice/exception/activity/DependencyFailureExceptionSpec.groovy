package chorestogetherapiservice.exception.activity

import spock.lang.Specification
import spock.lang.Subject

class DependencyFailureExceptionSpec extends Specification {

    def message = "error message"

    def cause = new Exception()

    @Subject
    def dependencyFailureException

    def "test dependencyFailureException initialization"() {
        given:
        dependencyFailureException = new DependencyFailureException(message, cause)

        expect:
        dependencyFailureException.message == message
        dependencyFailureException.cause == cause
    }

}
