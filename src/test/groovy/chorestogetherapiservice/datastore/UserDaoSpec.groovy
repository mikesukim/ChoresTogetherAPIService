package chorestogetherapiservice.datastore

import chorestogetherapiservice.TestingConstant
import chorestogetherapiservice.domain.ImmutableToken
import chorestogetherapiservice.domain.ImmutableUserEmail
import chorestogetherapiservice.exception.datastore.ItemAlreadyExistException
import chorestogetherapiservice.exception.datastore.NoItemFoundException
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException
import software.amazon.awssdk.core.pagination.sync.SdkIterable
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.model.Page
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Subject

class UserDaoSpec extends Specification {

    def email = "mikesungunkim@gmail.com"

    def rawToken = "token"

    def uid = "123"

    def tableMock = Mock(DynamoDbTable<UserItem>.class)

    def gsiMock = Mock(DynamoDbIndex<UserItem>.class)

    def dynamoDbClientMock = Mock(DynamoDbEnhancedClient.class) {
        it.table("user", _ as TableSchema) >> tableMock
        tableMock.index("user_by_email") >> gsiMock
    }

    @Shared
    def user = TestingConstant.USER

    @Subject
    UserDao userDao = new UserDao(dynamoDbClientMock)

    def "test success getUser"() {
        given:
        def userEmail = ImmutableUserEmail.builder().email(email).build()
        ArrayList<UserItem> userItems = new ArrayList<>()
        ArrayList<Page<UserItem>> pages = new ArrayList<>()
        userItems.add(TestingConstant.USER_ITEM)
        Page<UserItem> page = new Page<>(userItems, null)
        pages.add(page)

        when:
        def result = userDao.get(userEmail)
        def expectedResult = TestingConstant.USER_ITEM

        then:
        result == expectedResult

        1 * tableMock.index("user_by_email").query(_ as QueryEnhancedRequest)
                >> Mock(SdkIterable<Page<UserItem>>.class){it.stream() >> pages.stream()}
        0 * _
    }

    def "test getUser failure when multiple users with same email"() {
        given:
        def userEmail = ImmutableUserEmail.builder().email(email).build()
        ArrayList<UserItem> userItems = new ArrayList<>()
        ArrayList<Page<UserItem>> pages = new ArrayList<>()
        userItems.add(TestingConstant.USER_ITEM)
        userItems.add(TestingConstant.USER_ITEM_2)
        Page<UserItem> page = new Page<>(userItems, null)
        pages.add(page)

        when:
        userDao.get(userEmail)

        then:
        thrown(ItemAlreadyExistException)

        1 * tableMock.index("user_by_email").query(_ as QueryEnhancedRequest)
                >> Mock(SdkIterable<Page<UserItem>>.class){it.stream() >> pages.stream()}
        0 * _
    }

    def "test getUser failure when no item was found"() {
        given:
        def userEmailMock = ImmutableUserEmail.builder().email(email).build()
        ArrayList<UserItem> userItems = new ArrayList<>()
        ArrayList<Page<UserItem>> pages = new ArrayList<>()
        userItems.add(TestingConstant.USER_ITEM)

        when:
        userDao.get(userEmailMock)

        then:
        thrown(NoItemFoundException)

        1 * tableMock.index("user_by_email").query(_ as QueryEnhancedRequest)
                >> Mock(SdkIterable<Page<UserItem>>.class){it.stream() >> pages.stream()}
        0 * _
    }

    def "test when dynamodb internal failure"() {
        given:
        def userEmailMock = ImmutableUserEmail.builder().email(email).build()

        when:
        userDao.get(userEmailMock)

        then:
        thrown(DependencyFailureInternalException)

        1 * tableMock.index("user_by_email").query(_ as QueryEnhancedRequest)
                >> {new Exception()}
        0 * _
    }

    def "test success create user"() {
        given:
        def token = ImmutableToken.builder().token(rawToken).build()

        when:
        userDao.create(user, token)

        then:
        1 * tableMock.putItem(_)
        0 * _
    }

    def "test createUser when item already exist"() {
        given:
        def token = ImmutableToken.builder().token(rawToken).build()

        when:
        userDao.create(user, token)

        then:
        thrown(ItemAlreadyExistException)

        1 * tableMock.putItem(_) >> {throw ConditionalCheckFailedException.builder().build()}
        0 * _
    }

    def "test createUser when dynamodb internal failure occurs"() {
        given:
        def token = ImmutableToken.builder().token(rawToken).build()

        when:
        userDao.create(user, token)

        then:
        thrown(DependencyFailureInternalException)

        1 * tableMock.putItem(_) >> {throw new Exception()}
        0 * _
    }
}
