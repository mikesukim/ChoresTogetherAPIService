package chorestogetherapiservice.logic

import chorestogetherapiservice.datastore.UserDao
import chorestogetherapiservice.datastore.UserItemBuilder
import chorestogetherapiservice.domain.ResponseEntity
import chorestogetherapiservice.domain.SocialIdToken
import chorestogetherapiservice.domain.SocialLoginServiceType
import chorestogetherapiservice.domain.Token
import chorestogetherapiservice.domain.User
import chorestogetherapiservice.domain.UserEmail
import chorestogetherapiservice.exception.datastore.NoItemFoundException
import chorestogetherapiservice.exception.sociallogin.SocialIdTokenValidationExecption
import chorestogetherapiservice.factory.SocialIdTokenVerifierFactory
import chorestogetherapiservice.handler.JwtHandler
import chorestogetherapiservice.handler.ResponseHandler
import chorestogetherapiservice.verifier.SocialIdTokenVerifier
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException
import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.core.Response
import java.time.Instant

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

    def "test success loginUser when user already exist in database"() {
        given:
        def expectedResult = Response.status(200).entity(responseEntityMock).build()
        def rawEmail = "rawEmail@gmail.com"
        def rawToken = "token"
        def optionalUserEmail = Optional.of(rawEmail)
        def randomTime = Instant.ofEpochMilli(1)
        def userItem = new UserItemBuilder().email(rawEmail).registrationDate(randomTime).token(rawToken).build()

        when:
        def result = loginUserLogic.loginUser(SocialLoginServiceType.GOOGLE, socialIdTokenMock)

        then:
        result == expectedResult

        1 * socialIdTokenVerifierFactoryMock.getSocialIdTokenVerifier(SocialLoginServiceType.GOOGLE) >>
                socialIdTokenVerifierMock
        1 * socialIdTokenVerifierMock.verify(socialIdTokenMock) >> optionalUserEmail
        1 * userDaoMock.get({it.getEmail() == rawEmail} as UserEmail) >> userItem
        1 * responseHandlerMock.generateSuccessResponseWith({it.getToken() == rawToken} as Token) >> expectedResult
        0 * _
    }

    def "test success loginUser when user is new to our service"() {
        given:
        def expectedResult = Response.status(200).entity(responseEntityMock).build()
        def rawEmail = "rawEmail@gmail.com"
        def rawToken = "token"
        def optionalUserEmail = Optional.of(rawEmail)

        when:
        def result = loginUserLogic.loginUser(SocialLoginServiceType.GOOGLE, socialIdTokenMock)

        then:
        result == expectedResult

        1 * socialIdTokenVerifierFactoryMock.getSocialIdTokenVerifier(SocialLoginServiceType.GOOGLE) >>
                socialIdTokenVerifierMock
        1 * socialIdTokenVerifierMock.verify(socialIdTokenMock) >> optionalUserEmail
        1 * userDaoMock.get({it.getEmail() == rawEmail} as UserEmail) >> {throw new NoItemFoundException("")}
        1 * jwtHandlerMock.generateJwt({it.getEmail() == rawEmail} as User) >> rawToken
        1 * userDaoMock.create({it.getEmail() == rawEmail} as User, {it.getToken() == rawToken} as Token)
        1 * responseHandlerMock.generateSuccessResponseWith({it.getToken() == rawToken} as Token) >> expectedResult
        0 * _
    }

    def "test loginUser fail when exception occurred during createUser"() {
        given:
        def rawEmail = "rawEmail@gmail.com"
        def rawToken = "token"
        def optionalUserEmail = Optional.of(rawEmail)

        when:
        loginUserLogic.loginUser(SocialLoginServiceType.GOOGLE, socialIdTokenMock)

        then:
        thrown(Exception)

        1 * socialIdTokenVerifierFactoryMock.getSocialIdTokenVerifier(SocialLoginServiceType.GOOGLE) >>
                socialIdTokenVerifierMock
        1 * socialIdTokenVerifierMock.verify(socialIdTokenMock) >> optionalUserEmail
        1 * userDaoMock.get({it.getEmail() == rawEmail} as UserEmail) >> {throw new NoItemFoundException("")}
        1 * jwtHandlerMock.generateJwt({it.getEmail() == rawEmail} as User) >> rawToken
        1 * userDaoMock.create({it.getEmail() == rawEmail} as User, {it.getToken() == rawToken} as Token) >> {throw new Exception()}
        0 * _
    }

    def "test loginUser fail when exception occurred during getUser"() {
        given:
        def rawEmail = "rawEmail@gmail.com"
        def optionalUserEmail = Optional.of(rawEmail)

        when:
        loginUserLogic.loginUser(SocialLoginServiceType.GOOGLE, socialIdTokenMock)

        then:
        thrown(Exception)

        1 * socialIdTokenVerifierFactoryMock.getSocialIdTokenVerifier(SocialLoginServiceType.GOOGLE) >>
                socialIdTokenVerifierMock
        1 * socialIdTokenVerifierMock.verify(socialIdTokenMock) >> optionalUserEmail
        1 * userDaoMock.get({it.getEmail() == rawEmail} as UserEmail) >> {throw new DynamoDbException("")}
        0 * _
    }

    def "test loginUser fail when ID-Token's email payload is empty"() {
        when:
        loginUserLogic.loginUser(SocialLoginServiceType.GOOGLE, socialIdTokenMock)

        then:
        thrown(Exception)

        1 * socialIdTokenVerifierFactoryMock.getSocialIdTokenVerifier(SocialLoginServiceType.GOOGLE) >>
                socialIdTokenVerifierMock
        1 * socialIdTokenVerifierMock.verify(socialIdTokenMock) >> Optional.empty()
        0 * _
    }

    def "test loginUser fail when exception occurred during ID-token validation"() {
        when:
        loginUserLogic.loginUser(SocialLoginServiceType.GOOGLE, socialIdTokenMock)

        then:
        thrown(Exception)

        1 * socialIdTokenVerifierFactoryMock.getSocialIdTokenVerifier(SocialLoginServiceType.GOOGLE) >>
                socialIdTokenVerifierMock
        1 * socialIdTokenVerifierMock.verify(socialIdTokenMock) >> {throw new SocialIdTokenValidationExecption("")}
        0 * _
    }
}
