package chorestogetherapiservice.exception.dependency

import spock.lang.Specification
import spock.lang.Subject

class DependencyFailureInternalExceptionSpec extends Specification {

    def message = "error message"

    @Subject
    def dependencyFailureInternalException

    def "test dependencyFailureInternalException initialization"() {
        given:
        dependencyFailureInternalException = new DependencyFailureInternalException(message)

        expect:
        dependencyFailureInternalException.message == message
    }

}
