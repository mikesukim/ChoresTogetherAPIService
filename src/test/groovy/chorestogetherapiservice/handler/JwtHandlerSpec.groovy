package chorestogetherapiservice.handler

import chorestogetherapiservice.TestingConstant
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.JwtBuilder
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.MalformedJwtException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class JwtHandlerSpec extends Specification {

    @Shared
    private final String JWT_HEADER_ISSUER = "ChoresTogetherAPIService"
    @Shared
    private final String JWT_HEADER_AUDIENCE = "ChoresTogether"

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
        1 * jwtBuilderMock.setSubject(user.getUid().toString()) >> jwtBuilderMock
        1 * jwtBuilderMock.setAudience(JWT_HEADER_AUDIENCE) >> jwtBuilderMock
        1 * jwtBuilderMock.setIssuedAt(_) >> jwtBuilderMock
        1 * jwtBuilderMock.compact() >> expectedToken
        0 * _

    }

    def "test parsing Jwt succeed"() {
        given:
        def token = "fakeToken"

        def claimMock = Mock(Jws.class)
        def claimBodyMock = Mock(Claims.class)

        when:
        jwtHandler.parseJwt(token)

        then:
        1 * jwtParserMock.parseClaimsJws(token) >> claimMock
        3 * claimMock.getBody() >> claimBodyMock
        1 * claimBodyMock.getAudience() >> JWT_HEADER_AUDIENCE
        1 * claimBodyMock.getIssuer() >> JWT_HEADER_ISSUER
        1 * claimBodyMock.getSubject() >> TestingConstant.UID
        0 * _
    }

    def "test when parsing failed"() {
        given:
        def token = "fakeToken"

        when:
        jwtHandler.parseJwt(token)

        then:
        thrown(JwtException)

        1 * jwtParserMock.parseClaimsJws(token) >> {throw new MalformedJwtException("error message")}
        0 * _
    }

    def "test when audience is not matched"() {
        given:
        def token = "fakeToken"

        def claimMock = Mock(Jws<Claims>.class)
        def claimBodyMock = Mock(Claims.class)

        when:
        jwtHandler.parseJwt(token)

        then:
        thrown(JwtException)

        1 * jwtParserMock.parseClaimsJws(token) >> claimMock
        1 * claimMock.getBody() >> claimBodyMock
        1 * claimBodyMock.getAudience() >> "wrong Audience"
        0 * _
    }

    def "test when issuer is not matched"() {
        given:
        def token = "fakeToken"

        def claimMock = Mock(Jws<Claims>.class)
        def claimBodyMock = Mock(Claims.class)

        when:
        jwtHandler.parseJwt(token)

        then:
        thrown(JwtException)

        1 * jwtParserMock.parseClaimsJws(token) >> claimMock
        2 * claimMock.getBody() >> claimBodyMock
        1 * claimBodyMock.getAudience() >> JWT_HEADER_AUDIENCE
        1 * claimBodyMock.getIssuer() >> "wrong issuer"
        0 * _
    }

    def "test when Jwt subject is empty"() {
        given:
        def token = "fakeToken"

        def claimMock = Mock(Jws.class)
        def claimBodyMock = Mock(Claims.class)

        when:
        jwtHandler.parseJwt(token)

        then:
        thrown(JwtException)

        1 * jwtParserMock.parseClaimsJws(token) >> claimMock
        3 * claimMock.getBody() >> claimBodyMock
        1 * claimBodyMock.getAudience() >> JWT_HEADER_AUDIENCE
        1 * claimBodyMock.getIssuer() >> JWT_HEADER_ISSUER
        1 * claimBodyMock.getSubject() >> null
        0 * _
    }

}
