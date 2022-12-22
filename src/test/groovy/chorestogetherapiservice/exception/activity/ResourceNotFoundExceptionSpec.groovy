package chorestogetherapiservice.exception.activity

import spock.lang.Specification
import spock.lang.Subject

class ResourceNotFoundExceptionSpec extends Specification {

    def message = "error message"

    def cause = new Exception()

    @Subject
    def resourceNotFoundException

    def "test resourceNotFoundException initialization"() {
        given:
        resourceNotFoundException = new ResourceNotFoundException(message, cause)

        expect:
        resourceNotFoundException.message == message
        resourceNotFoundException.cause == cause
    }
}
