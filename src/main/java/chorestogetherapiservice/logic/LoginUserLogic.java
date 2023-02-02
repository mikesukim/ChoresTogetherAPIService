package chorestogetherapiservice.logic;

import chorestogetherapiservice.datastore.UserDao;
import chorestogetherapiservice.domain.SocialIdToken;
import chorestogetherapiservice.domain.SocialLoginServiceType;
import chorestogetherapiservice.factory.SocialIdTokenVerifierFactory;
import chorestogetherapiservice.handler.JwtHandler;
import chorestogetherapiservice.handler.ResponseHandler;
import chorestogetherapiservice.verifier.SocialIdTokenVerifier;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.core.Response;


/** Business logic for LoginUser API. */
public class LoginUserLogic {

  ResponseHandler responseHandler;

  SocialIdTokenVerifierFactory socialIdTokenVerifierFactory;

  JwtHandler jwtHandler;

  UserDao userDao;

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Inject
  public LoginUserLogic(ResponseHandler responseHandler,
                        SocialIdTokenVerifierFactory socialIdTokenVerifierFactory,
                        JwtHandler jwtHandler,
                        UserDao userDao) {
    this.responseHandler = responseHandler;
    this.socialIdTokenVerifierFactory = socialIdTokenVerifierFactory;
    this.jwtHandler = jwtHandler;
    this.userDao = userDao;
  }

  /**
   * TODO: fix javadoc.
   * Skeleton LoginUser */
  public Response loginUser(SocialLoginServiceType socialLoginServiceType,
                            SocialIdToken socialIdToken) {

    SocialIdTokenVerifier socialIdTokenVerifier =
        socialIdTokenVerifierFactory.getSocialIdTokenVerifier(socialLoginServiceType);

    Optional<String> rawUserEmail = socialIdTokenVerifier.verify(socialIdToken);

    //TODO : add logic of checking if user exist at database.
    // if not exist, register user, generate new token and return a new token.
    // if exist, then get token from database and return that token.

    return responseHandler.generateFailResponseWith("not implemented yet");
  }
}