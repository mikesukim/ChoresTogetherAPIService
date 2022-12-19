package chorestogetherapiservice.logic;

import chorestogetherapiservice.datastore.UserDao;
import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.domain.UserEmail;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GetUserLogic {

    private final UserDao userDao;

    @Inject
    GetUserLogic(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser(UserEmail userEmail) {
        return userDao.read(userEmail);
    }
}
