package chorestogetherapiservice.module

import com.google.inject.Guice
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.WeakKeyException
import spock.lang.Specification
import spock.lang.Subject

import java.security.Key

class JwtModuleSpec extends Specification {
    @Subject
    def jwtModule = new JwtModule()

    def injector =  Guice.createInjector(jwtModule)

    def "test jwtBuilder injection"() {
        when:
        def result = injector.getInstance(JwtBuilder.class)

        then:
        result instanceof JwtBuilder
    }

    def "test jwtParser injection"() {
        when:
        def result = injector.getInstance(JwtParser.class)

        then:
        result instanceof JwtParser
    }

    def "test raising exception when key size is not long enough"() {
        given:
        def mockStringSecretKey = "thisIsShortKey"

        when:
        jwtModule.jwtBuilder(mockStringSecretKey)

        then:
        thrown(WeakKeyException)
    }

    def "test success generating jwtBuilder when key size is long enough"() {
        given:
        def mockSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        def mockStringSecretKey = Encoders.BASE64.encode(mockSecretKey.getEncoded())

        when:
        def result = jwtModule.jwtBuilder(mockStringSecretKey)

        then:
        result instanceof JwtBuilder
    }
}
