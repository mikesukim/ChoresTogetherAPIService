package chorestogetherapiservice.exception.datastore

import spock.lang.Specification
import spock.lang.Subject

class NoItemFoundExceptionSpec extends Specification {

    def message = "error message"

    @Subject
    def noItemFoundException

    def "test noItemFoundException initialization"() {
        given:
        noItemFoundException = new NoItemFoundException(message)

        expect:
        noItemFoundException.message == message
    }

}
