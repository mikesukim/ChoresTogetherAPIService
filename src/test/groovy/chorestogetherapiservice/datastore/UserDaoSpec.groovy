package chorestogetherapiservice.datastore

import chorestogetherapiservice.domain.UserEmail
import spock.lang.Specification
import spock.lang.Subject

class UserDaoSpec extends Specification {

    @Subject
    UserDao userDaoMock = Mock(UserDao.class)

    def "test success read"(){
        given:
        def userEmailMock = new UserEmail()

        when:
        def result = userDaoMock.read(userEmailMock)
        def expectedResult = null

        then:
        result == expectedResult
    }
}
