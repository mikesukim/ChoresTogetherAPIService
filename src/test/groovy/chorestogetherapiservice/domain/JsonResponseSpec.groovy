package chorestogetherapiservice.domain

import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class JsonResponseSpec extends Specification {

    private final String SUCCESS_STATUS = "success"

    @Subject
    JsonResponse jsonResponse

    @Shared
    def dataMock = new HashMap<String, String>()
    @Shared
    def messageMock = "message"

    def "test successful constructing jsonResponse"() {

        when:
        jsonResponse = new JsonResponse(SUCCESS_STATUS, message, data)

        then:
        notThrown(NullPointerException)

        where:
        data                    | message
        Optional.of(dataMock)   | Optional.of(messageMock)
        Optional.empty()        | Optional.empty()
    }
}
