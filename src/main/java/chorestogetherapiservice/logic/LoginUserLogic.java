package chorestogetherapiservice.logic;

import chorestogetherapiservice.datastore.UserDao;
import chorestogetherapiservice.datastore.UserItem;
import chorestogetherapiservice.domain.ImmutableToken;
import chorestogetherapiservice.domain.ImmutableUser;
import chorestogetherapiservice.domain.ImmutableUserEmail;
import chorestogetherapiservice.domain.SocialIdToken;
import chorestogetherapiservice.domain.SocialLoginServiceType;
import chorestogetherapiservice.domain.Token;
import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.domain.UserEmail;
import chorestogetherapiservice.exception.datastore.NoItemFoundException;
import chorestogetherapiservice.exception.sociallogin.SocialIdTokenValidationExecption;
import chorestogetherapiservice.factory.SocialIdTokenVerifierFactory;
import chorestogetherapiservice.handler.JwtHandler;
import chorestogetherapiservice.handler.ResponseHandler;
import chorestogetherapiservice.verifier.SocialIdTokenVerifier;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
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
   * This logic is to login user with ID-Token which provided by social service.
   * (currently Google is only supported.)
   *
   * @param  socialLoginServiceType social service which generated ID-TOKEN.
   * @param  socialIdToken          user's Social ID-TOKEN
   * @return  Response which contains our service's token as entity. */
  public Response loginUser(SocialLoginServiceType socialLoginServiceType,
                            SocialIdToken socialIdToken) {

    // Verify ID-Token and retrieve userEmail from it
    SocialIdTokenVerifier socialIdTokenVerifier =
        socialIdTokenVerifierFactory.getSocialIdTokenVerifier(socialLoginServiceType);
    Optional<String> rawUserEmail = socialIdTokenVerifier.verify(socialIdToken);
    if (rawUserEmail.isEmpty()) {
      throw new SocialIdTokenValidationExecption("ID Token does not contain userEmail");
    }

    // Check if user already exists
    UserEmail userEmail = ImmutableUserEmail.builder().email(rawUserEmail.get()).build();
    Optional<UserItem> userItem;
    try {
      userItem = Optional.of(userDao.get(userEmail));
    } catch (NoItemFoundException e) {
      userItem = Optional.empty();
    }

    // If exist, return user's token
    if (userItem.isPresent()) {
      String rawToken = userItem.get().getToken();
      Token token = ImmutableToken.builder().token(rawToken).build();
      return responseHandler.generateSuccessResponseWith(token);
    }

    // If not exist, register user
    User user = ImmutableUser.builder()
        .email(rawUserEmail.get())
        .uid(NanoIdUtils.randomNanoId())
        .build();

    String rawToken = jwtHandler.generateJwt(user);
    Token token = ImmutableToken.builder().token(rawToken).build();
    userDao.create(user, token);
    return responseHandler.generateSuccessResponseWith(token);
  }
}