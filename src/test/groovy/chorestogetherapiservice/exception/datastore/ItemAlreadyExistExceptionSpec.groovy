package chorestogetherapiservice.exception.datastore

import spock.lang.Specification
import spock.lang.Subject

class ItemAlreadyExistExceptionSpec extends Specification {

    def message = "error message"

    @Subject
    def itemAlreadyExistException

    def "test noItemFoundException initialization"() {
        given:
        itemAlreadyExistException = new ItemAlreadyExistException(message)

        expect:
        itemAlreadyExistException.message == message
    }
}
