package chorestogetherapiservice.logic;

import chorestogetherapiservice.datastore.UserDao;
import chorestogetherapiservice.datastore.UserItem;
import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.domain.UserEmail;
import chorestogetherapiservice.exception.DependencyFailureInternalException;
import chorestogetherapiservice.exception.datastore.NoItemFoundException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class GetUserLogic {

    private final UserDao userDao;

    @Inject
    GetUserLogic(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser(UserEmail userEmail) throws DependencyFailureInternalException, NoItemFoundException {
        Optional<UserItem> userItem = userDao.get(userEmail);
        if (userItem.isEmpty()){
            throw new NoItemFoundException("No user was found");
        }
        //TODO : decouple converting logic
        return new User(userItem.get().getEmail());
    }
}
