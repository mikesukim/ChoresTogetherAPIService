package chorestogetherapiservice.logic;

import chorestogetherapiservice.datastore.UserDao;
import chorestogetherapiservice.datastore.UserItem;
import chorestogetherapiservice.domain.ImmutableUserEmail;
import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.exception.datastore.NoItemFoundException;
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

/** Business logic for GetUser API. */
@Singleton
public class GetUserLogic {

  private final UserDao userDao;

  @Inject
  GetUserLogic(UserDao userDao) {
    this.userDao = userDao;
  }

  /**
   * getUser business logic.
   *
   * @param  immutableUserEmail user's email as UserEmail type.
   * @return                    User if user exists, if not, raise NoItemFoundException.
   * */
  public User getUser(ImmutableUserEmail immutableUserEmail) throws
      DependencyFailureInternalException, NoItemFoundException {
    Optional<UserItem> userItem = userDao.get(immutableUserEmail);
    if (userItem.isEmpty()) {
      throw new NoItemFoundException("No user was found");
    }
    //TODO: decouple converting logic
    return new User(userItem.get().getEmail());
  }
}
