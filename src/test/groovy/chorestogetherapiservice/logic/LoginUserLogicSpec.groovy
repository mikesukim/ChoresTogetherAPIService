package chorestogetherapiservice.logic

import chorestogetherapiservice.datastore.UserDao
import chorestogetherapiservice.domain.ResponseEntity
import chorestogetherapiservice.domain.SocialIdToken
import chorestogetherapiservice.domain.SocialLoginServiceType
import chorestogetherapiservice.factory.SocialIdTokenVerifierFactory
import chorestogetherapiservice.handler.JwtHandler
import chorestogetherapiservice.handler.ResponseHandler
import chorestogetherapiservice.verifier.SocialIdTokenVerifier
import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.core.Response

class LoginUserLogicSpec extends Specification {

    def responseHandlerMock = Mock(ResponseHandler.class)

    def socialIdTokenVerifierFactoryMock = Mock(SocialIdTokenVerifierFactory.class)

    def jwtHandlerMock = Mock(JwtHandler.class)

    def userDaoMock = Mock(UserDao.class)

    def socialIdTokenMock = Mock(SocialIdToken.class)

    def responseEntityMock = Mock(ResponseEntity.class)

    def socialIdTokenVerifierMock = Mock(SocialIdTokenVerifier.class)

    @Subject
    def loginUserLogic = new LoginUserLogic(
            responseHandlerMock,
            socialIdTokenVerifierFactoryMock,
            jwtHandlerMock,
            userDaoMock)


    def "test skeleton loginUser"() {
        given:
        def expectedResult = Response.status(500).entity(responseEntityMock).build()
        def rawUserEmailMock = GroovyMock(Optional<String>.class)

        when:
        def result = loginUserLogic.loginUser(SocialLoginServiceType.GOOGLE, socialIdTokenMock)

        then:
        result == expectedResult

        1 * socialIdTokenVerifierFactoryMock.getSocialIdTokenVerifier(SocialLoginServiceType.GOOGLE) >>
                socialIdTokenVerifierMock
        1 * socialIdTokenVerifierMock.verify(socialIdTokenMock) >> rawUserEmailMock
        1 * responseHandlerMock.generateFailResponseWith(_) >> expectedResult
        0 * _
    }
}
