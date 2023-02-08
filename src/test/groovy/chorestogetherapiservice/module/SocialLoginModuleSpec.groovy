package chorestogetherapiservice.module

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.inject.Guice
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import spock.lang.Specification
import spock.lang.Subject

class SocialLoginModuleSpec extends Specification {

    @Subject
    def socialLoginModule = new SocialLoginModule()

    def injector =  Guice.createInjector(socialLoginModule)


    def "test GoogleIdTokenVerifier injection"() {
        when:
        def result = injector.getInstance(GoogleIdTokenVerifier.class)

        then:
        result instanceof GoogleIdTokenVerifier
    }
}
