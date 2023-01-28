package chorestogetherapiservice.logic;

import chorestogetherapiservice.datastore.UserDao;
import chorestogetherapiservice.datastore.UserItem;
import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.domain.UserEmail;
import chorestogetherapiservice.exception.datastore.NoItemFoundException;
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException;
import chorestogetherapiservice.handler.ResponseHandler;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Response;

/** Business logic for GetUser API. */
@Singleton
public class GetUserLogic {

  private final UserDao userDao;

  ResponseHandler responseHandler;

  @Inject
  GetUserLogic(UserDao userDao, ResponseHandler responseHandler) {
    this.userDao = userDao;
    this.responseHandler = responseHandler;
  }

  /**
   * getUser business logic.
   *
   * @param  userEmail user's email as UserEmail type.
   * @return                    User if user exists, if not, raise NoItemFoundException.
   * */
  public Response getUser(UserEmail userEmail) throws
      DependencyFailureInternalException, NoItemFoundException {
    User user;
    try {
      UserItem userItem = userDao.get(userEmail);
      user = User.of(userItem);
    } catch (DependencyFailureInternalException e) {
      return responseHandler.generateFailResponseWith(e.getMessage());
    } catch (NoItemFoundException e) {
      return responseHandler.generateResourceNotFoundErrorResponseWith(e.getMessage());
    }
    return responseHandler.generateSuccessResponseWith(user);
  }
}
