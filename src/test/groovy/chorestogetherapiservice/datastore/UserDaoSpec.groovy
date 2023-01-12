package chorestogetherapiservice.datastore

import chorestogetherapiservice.domain.ImmutableUserEmail
import chorestogetherapiservice.domain.UserEmail
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
        def userItem = new UserItem()
        userItem.setEmail(email)
        def userEmail = ImmutableUserEmail.builder().email(email).build()

        when:
        def result = userDao.get(userEmail)
        def expectedResult = Optional<UserItem>.of(userItem)

        then:
        result == expectedResult

        1 * tableMock.getItem(_ as Consumer<GetItemEnhancedRequest.Builder>) >> userItem
        0 * _
    }

    def "test when no item was found"() {
        given:
        def userItem = new UserItem()
        userItem.setEmail(email)
        def userEmailMock = ImmutableUserEmail.builder().email(email).build()

        when:
        def result = userDao.get(userEmailMock)
        def expectedResult = Optional<UserItem>.empty()

        then:
        result == expectedResult

        1 * tableMock.getItem(_ as Consumer<GetItemEnhancedRequest.Builder>) >> null
        0 * _
    }

    def "test when dynamodb internal failure"() {
        given:
        def userItem = new UserItem()
        userItem.setEmail(email)
        def userEmailMock = ImmutableUserEmail.builder().email(email).build()

        when:
        userDao.get(userEmailMock)

        then:
        thrown(DependencyFailureInternalException)

        1 * tableMock.getItem(_ as Consumer<GetItemEnhancedRequest.Builder>) >> new Exception()
        0 * _
    }
}
