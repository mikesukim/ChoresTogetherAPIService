package chorestogetherapiservice.util

import spock.lang.Specification
import spock.lang.Subject

class EnvValReaderSpec extends Specification {
    @Subject
    EnvValReader envValReader

    def "test getStage method"() {
        when:
        def result = EnvValReader.getStage()

        then:
        result != null
        result instanceof String
    }

    def "test getRegion method"() {
        when:
        def result = EnvValReader.getRegion()

        then:
        result != null
        result instanceof String
    }
}
