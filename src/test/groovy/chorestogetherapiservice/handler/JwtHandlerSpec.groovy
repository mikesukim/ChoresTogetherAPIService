package chorestogetherapiservice.handler

import chorestogetherapiservice.TestingConstant
import chorestogetherapiservice.domain.ImmutableUser
import chorestogetherapiservice.module.JwtModule
import com.google.inject.Guice
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.MalformedJwtException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class JwtHandlerSpec extends Specification {

    private final String JWT_HEADER_ISSUER = "ChoresAPIService"
    private final String JWT_HEADER_AUDIENCE = "ChoresAPIService"

    JwtParser jwtParserMock = Mock(JwtParser.class)
    JwtBuilder jwtBuilderMock = Mock(JwtBuilder.class)

    @Shared
    def user = TestingConstant.USER

    @Subject
    def jwtHandler = new JwtHandler(jwtParserMock, jwtBuilderMock)

    def "test success generating Jwt"() {
        given:
        def expectedToken = "token"

        when:
        def result = jwtHandler.generateJwt(user)

        then:
        result == expectedToken

        1 * jwtBuilderMock.setIssuer(JWT_HEADER_ISSUER) >> jwtBuilderMock
        1 * jwtBuilderMock.setSubject(user.toString()) >> jwtBuilderMock
        1 * jwtBuilderMock.setAudience(JWT_HEADER_AUDIENCE) >> jwtBuilderMock
        1 * jwtBuilderMock.setIssuedAt(_) >> jwtBuilderMock
        1 * jwtBuilderMock.compact() >> expectedToken
        0 * _

    }

    def "test parsing Jwt succeed"() {
        given:
        def token = "fakeToken"

        def claimMock = Mock(Jws<Claims>.class)
        def claimBodyMock = Mock(Claims.class)

        when:
        jwtHandler.parseJwt(Optional<String>.of(token), user)

        then:
        1 * jwtParserMock.parseClaimsJws(token) >> claimMock
        1 * claimMock.getBody() >> claimBodyMock
        1 * claimBodyMock.getSubject() >> user.toString()
        0 * _
    }

    def "test when empty Jwt passed"() {
        when:
        jwtHandler.parseJwt(Optional<String>.empty(), user)

        then:
        thrown(JwtException)
    }

    def "test when parsing failed"() {
        given:
        def token = "fakeToken"

        when:
        jwtHandler.parseJwt(Optional<String>.of(token), user)

        then:
        thrown(JwtException)

        1 * jwtParserMock.parseClaimsJws(token) >> {throw new MalformedJwtException("error message")}
        0 * _
    }

    def "test when subject is not matched"() {
        given:
        def unknownUser = ImmutableUser.builder().email("unknown@email.com").build()
        def token = "fakeToken"

        def claimMock = Mock(Jws<Claims>.class)
        def claimBodyMock = Mock(Claims.class)

        when:
        jwtHandler.parseJwt(Optional<String>.of(token), user)

        then:
        thrown(JwtException)

        1 * jwtParserMock.parseClaimsJws(token) >> claimMock
        1 * claimMock.getBody() >> claimBodyMock
        1 * claimBodyMock.getSubject() >> unknownUser.toString()
        0 * _
    }

}
