package chorestogetherapiservice.logic;

import chorestogetherapiservice.datastore.UserDao;
import chorestogetherapiservice.domain.ImmutableToken;
import chorestogetherapiservice.domain.Token;
import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.exception.datastore.ItemAlreadyExistException;
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException;
import chorestogetherapiservice.handler.JwtHandler;
import chorestogetherapiservice.handler.ResponseHandler;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

/** Business logic for GetUser API. */
@Singleton
public class CreateUserLogic {

  private final UserDao userDao;
  private final ResponseHandler responseHandler;
  private final JwtHandler jwtHandler;

  @Inject
  CreateUserLogic(UserDao userDao, ResponseHandler responseHandler, JwtHandler jwtHandler) {
    this.userDao = userDao;
    this.responseHandler = responseHandler;
    this.jwtHandler = jwtHandler;
  }

  /**
   * createUser business logic.
   */
  public Response createUser(User user) throws
      DependencyFailureInternalException, ItemAlreadyExistException {
    try {
      userDao.create(user);
    } catch (DependencyFailureInternalException e) {
      return responseHandler.generateFailResponseWith(e.getMessage());
    } catch (ItemAlreadyExistException e) {
      return responseHandler.generateBadRequestErrorResponseWith(e.getMessage());
    }
    String rawToken = jwtHandler.generateJwt(user);
    Token token = ImmutableToken.builder().token(rawToken).build();
    return responseHandler.generateSuccessResponseWith(token);
  }
}
