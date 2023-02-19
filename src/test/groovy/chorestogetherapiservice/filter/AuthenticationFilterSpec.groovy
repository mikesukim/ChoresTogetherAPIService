package chorestogetherapiservice.filter

import chorestogetherapiservice.TestingConstant
import chorestogetherapiservice.exception.auth.NotAuthorizedException
import chorestogetherapiservice.handler.JwtHandler
import io.jsonwebtoken.JwtException
import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.core.HttpHeaders

class AuthenticationFilterSpec extends Specification {

    def jwtHandlerMock = Mock(JwtHandler.class)

    def containerRequestContextMock = Mock(ContainerRequestContext.class)

    @Subject
    def authenticationFilter = new AuthenticationFilter(jwtHandlerMock)


    def "test authentication success"() {
        when:
        authenticationFilter.filter(containerRequestContextMock)

        then:
        1 * containerRequestContextMock.getHeaderString(HttpHeaders.AUTHORIZATION) >> TestingConstant.AUTHORIZATION_HEADER
        1 * jwtHandlerMock.parseJwt(TestingConstant.TOKEN) >> TestingConstant.UID
        1 * containerRequestContextMock.setProperty("uid", TestingConstant.UID)
        0 * _
    }

    def "test authentication failed due to header wrong format"() {
        when:
        authenticationFilter.filter(containerRequestContextMock)

        then:
        thrown(NotAuthorizedException)

        1 * containerRequestContextMock.getHeaderString(HttpHeaders.AUTHORIZATION) >> header
        0 * _

        where:
        header << ["rawTokenWithoutBearer", "bearer wrongCase", "bearer ", " ", null]
    }

    def "test authentication token validation failed"() {
        when:
        authenticationFilter.filter(containerRequestContextMock)

        then:
        thrown(NotAuthorizedException)

        1 * containerRequestContextMock.getHeaderString(HttpHeaders.AUTHORIZATION) >> TestingConstant.AUTHORIZATION_HEADER
        1 * jwtHandlerMock.parseJwt(_) >> {throw new JwtException("")}
        0 * _
    }
}
