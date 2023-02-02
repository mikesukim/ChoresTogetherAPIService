package chorestogetherapiservice.domain

import spock.lang.Specification
import spock.lang.Subject

class SocialLoginServiceTypeSpec extends Specification {

    @Subject
    SocialLoginServiceType socialLoginServiceType

    def "test successful constructing socialLoginServiceType"() {
        when:
        socialLoginServiceType = service

        then:
        socialLoginServiceType.toString() == expectedResult

        where:
        service << [SocialLoginServiceType.GOOGLE, SocialLoginServiceType.KAKAO]
        expectedResult << ["GOOGLE", "KAKAO"]
    }

}
