package chorestogetherapiservice.logic;

import chorestogetherapiservice.datastore.UserDao;
import chorestogetherapiservice.datastore.UserItem;
import chorestogetherapiservice.domain.ImmutableUser;
import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.domain.UserEmail;
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
   * @param  userEmail user's email as UserEmail type.
   * @return                    User if user exists, if not, raise NoItemFoundException.
   * */
  public User getUser(UserEmail userEmail) throws
      DependencyFailureInternalException, NoItemFoundException {
    Optional<UserItem> userItem = userDao.get(userEmail);
    if (userItem.isEmpty()) {
      throw new NoItemFoundException("No user was found");
    }
    //TODO: decouple converting logic
    return ImmutableUser.builder().email(userItem.get().getEmail()).build();
  }
}
