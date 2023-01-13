package chorestogetherapiservice.domain

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class ResponseEntitySpec extends Specification {

    private final String SUCCESS_STATUS = "success"

    @Subject
    ResponseEntity responseEntity

    @Shared
    def dataMock = new HashMap<String, String>()
    @Shared
    def messageMock = "message"

    def "test successful constructing ResponseEntity"() {

        when:
        responseEntity = ImmutableResponseEntity.builder().status(SUCCESS_STATUS).message(messageMock).data(dataMock).build()

        then:
        notThrown(NullPointerException)

        where:
        data                    | message
        Optional.of(dataMock)   | Optional.of(messageMock)
        Optional.empty()        | Optional.empty()
    }
}
