package chorestogetherapiservice.logic;

import chorestogetherapiservice.datastore.UserDao;
import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.exception.datastore.ItemAlreadyExistException;
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException;
import javax.inject.Inject;
import javax.inject.Singleton;

/** Business logic for GetUser API. */
@Singleton
public class CreateUserLogic {

  private final UserDao userDao;

  @Inject
  CreateUserLogic(UserDao userDao) {
    this.userDao = userDao;
  }

  /**
   * createUser business logic.
   */
  public void createUser(User user) throws
      DependencyFailureInternalException, ItemAlreadyExistException {
    userDao.create(user);
  }
}
