package chorestogetherapiservice.logic

import chorestogetherapiservice.datastore.UserDao
import chorestogetherapiservice.domain.ImmutableUser
import chorestogetherapiservice.exception.datastore.ItemAlreadyExistException
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException
import spock.lang.Specification
import spock.lang.Subject

class CreateUserLogicSpec extends Specification {

    UserDao userDaoMock = Mock(UserDao.class)

    @Subject
    CreateUserLogic createUserLogic = new CreateUserLogic(userDaoMock)

    def "test success createUser"() {
        given:
        def user = ImmutableUser.builder().email("fake@gmail.com").build()

        when:
        createUserLogic.createUser(user)

        then:
        1 * userDaoMock.create(user)
        0 * _
    }

    def "test when ItemAlreadyExistException raise"() {
        given:
        def user = ImmutableUser.builder().email("fake@gmail.com").build()

        when:
        createUserLogic.createUser(user)

        then:
        thrown(ItemAlreadyExistException)

        1 * userDaoMock.create(user) >> {throw new ItemAlreadyExistException(_ as String)}
        0 * _
    }

    def "test when dependencyException raise"() {
        given:
        def user = ImmutableUser.builder().email("fake@gmail.com").build()
        def message = "error message"
        def cause = new Exception()

        when:
        createUserLogic.createUser(user)

        then:
        thrown(DependencyFailureInternalException)

        1 * userDaoMock.create(user) >> {throw new DependencyFailureInternalException(message,cause)}
        0 * _
    }
}
