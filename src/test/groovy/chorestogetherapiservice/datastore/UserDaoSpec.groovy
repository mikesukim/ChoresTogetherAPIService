package chorestogetherapiservice.datastore

import chorestogetherapiservice.domain.ImmutableUserEmail
import chorestogetherapiservice.domain.UserEmail
import chorestogetherapiservice.exception.datastore.NoItemFoundException
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest
import spock.lang.Specification
import spock.lang.Subject

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
        def userItem = new UserItemBuilder().email(email).build()
        def userEmail = ImmutableUserEmail.builder().email(email).build()

        when:
        def result = userDao.get(userEmail)
        def expectedResult = new UserItemBuilder().email(email).build()

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
}
