package chorestogetherapiservice.util

import spock.lang.Specification
import spock.lang.Subject

class StringPatternCheckUtilSpec extends Specification {

    @Subject
    StringPatternCheckUtil stringPatternCheckUtil

    def "test checkEmailPattern method"() {
        when:
        def result = StringPatternCheckUtil.checkEmailPattern(email)

        then:
        result == expectedResult

        where:
        email | expectedResult
        "asdf@adf.com" | true
        "asdf@" | false
        "asdf" | false
        "@asdf" | false
        "asdf@asdf." | false
    }
}
