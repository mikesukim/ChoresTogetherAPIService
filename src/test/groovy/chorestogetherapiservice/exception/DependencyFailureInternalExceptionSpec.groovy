package chorestogetherapiservice.exception

import spock.lang.Specification
import spock.lang.Subject

class DependencyFailureInternalExceptionSpec extends Specification {

    def message = "error message"

    def cause = new Exception()

    @Subject
    def dependencyFailureInternalException

    def "test dependencyFailureInternalException initialization"() {
        given:
        dependencyFailureInternalException = new DependencyFailureInternalException(message, cause)

        expect:
        dependencyFailureInternalException.message == message
        dependencyFailureInternalException.cause == cause
    }

}
