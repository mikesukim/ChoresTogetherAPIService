package chorestogetherapiservice.module

import chorestogetherapiservice.util.EnvValReader
import com.google.inject.Guice
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Encoders
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.WeakKeyException
import spock.lang.Specification
import spock.lang.Subject

class JwtModuleSpec extends Specification {
    @Subject
    def jwtModule = new JwtModule()

    def injector =  Guice.createInjector(jwtModule)

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

    def "test success generating jwtParser when key size is long enough"() {
        given:
        def mockSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)
        def mockStringSecretKey = Encoders.BASE64.encode(mockSecretKey.getEncoded())

        when:
        def result = jwtModule.jwtParser(mockStringSecretKey)

        then:
        result instanceof JwtParser
    }
}
