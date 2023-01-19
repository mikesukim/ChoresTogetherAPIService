package chorestogetherapiservice.datastore

import chorestogetherapiservice.domain.ImmutableUser
import chorestogetherapiservice.domain.ImmutableUserEmail
import chorestogetherapiservice.exception.datastore.ItemAlreadyExistException
import chorestogetherapiservice.exception.datastore.NoItemFoundException
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException
import spock.lang.Specification
import spock.lang.Subject

import java.time.Instant
import java.util.function.Consumer

class UserDaoSpec extends Specification {

    def email = "mikesungunkim@gmail.com"

    def tableMock = Mock(DynamoDbTable<UserItemSpec>.class)

    def dynamoDbClientMock = Mock(DynamoDbEnhancedClient.class) {
        it.table("User", _ as TableSchema) >> tableMock
    }

    @Subject
    UserDao userDao = new UserDao(dynamoDbClientMock)

    def "test success get user"() {
        given:
        def randomTime = Instant.ofEpochMilli(1)
        def userItem = new UserItemBuilder().email(email).registrationDate(randomTime).build()
        def userEmail = ImmutableUserEmail.builder().email(email).build()

        when:
        def result = userDao.get(userEmail)
        def expectedResult = new UserItemBuilder().email(email).registrationDate(randomTime).build()

        then:
        result == expectedResult

        1 * tableMock.getItem(_) >> userItem
        0 * _
    }

    def "test when no item was found"() {
        given:
        def userEmailMock = ImmutableUserEmail.builder().email(email).build()

        when:
        userDao.get(userEmailMock)

        then:
        thrown(NoItemFoundException)

        1 * tableMock.getItem(_) >> null
        0 * _
    }

    def "test when dynamodb internal failure"() {
        given:
        def userEmailMock = ImmutableUserEmail.builder().email(email).build()

        when:
        userDao.get(userEmailMock)

        then:
        thrown(DependencyFailureInternalException)

        1 * tableMock.getItem(_) >> new Exception()
        0 * _
    }

    def "test success create user"() {
        given:
        def user = ImmutableUser.builder().email(email).build()

        when:
        userDao.create(user)

        then:
        1 * tableMock.putItem(_)
        0 * _
    }

    def "test createUser when item already exist"() {
        given:
        def user = ImmutableUser.builder().email(email).build()

        when:
        userDao.create(user)

        then:
        thrown(ItemAlreadyExistException)

        1 * tableMock.putItem(_) >> {throw ConditionalCheckFailedException.builder().build()}
        0 * _
    }

    def "test createUser when dynamodb internal failure occurs"() {
        given:
        def user = ImmutableUser.builder().email(email).build()

        when:
        userDao.create(user)

        then:
        thrown(DependencyFailureInternalException)

        1 * tableMock.putItem(_) >> {throw new Exception()}
        0 * _
    }
}
