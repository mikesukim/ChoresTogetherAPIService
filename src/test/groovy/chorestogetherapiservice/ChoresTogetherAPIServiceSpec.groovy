package chorestogetherapiservice

import spock.lang.Specification
import spock.lang.Subject


class ChoresTogetherAPIServiceSpec extends Specification {

    @Subject
    ChoresTogetherAPIService choresTogetherAPIService

    def "test sayHello"() {
        when:
        choresTogetherAPIService = new ChoresTogetherAPIService()
        def result = choresTogetherAPIService.sayHello()

        then:
        result == "SayHello"
    }
}
