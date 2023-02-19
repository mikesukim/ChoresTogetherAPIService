package chorestogetherapiservice.exception.auth

import spock.lang.Specification
import spock.lang.Subject

class NotAuthorizedExceptionSpec extends Specification {
    def message = "error message"

    @Subject
    def notAuthorizedExceptionSpec

    def "test noItemFoundException initialization"() {
        given:
        notAuthorizedExceptionSpec = new NotAuthorizedException(message)

        expect:
        notAuthorizedExceptionSpec.message == message
    }
}
