package chorestogetherapiservice.logic

import chorestogetherapiservice.datastore.UserDao
import chorestogetherapiservice.domain.User
import chorestogetherapiservice.domain.UserEmail
import spock.lang.Specification
import spock.lang.Subject

class GetUserLogicSpec extends Specification {

    UserDao userDaoMock = Mock(UserDao.class)

    @Subject
    GetUserLogic getUserLogic = new GetUserLogic(userDaoMock)

    def "test success getUser"() {
        given:
        def userMock = new User()
        def userEmailMock = new UserEmail()

        when:
        def result = getUserLogic.getUser(userEmailMock)
        def expectedResult = userMock

        then:
        result == expectedResult

        1 * userDaoMock.read(userEmailMock) >> userMock
        0 * _
    }
}
