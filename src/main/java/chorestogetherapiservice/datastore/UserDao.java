package chorestogetherapiservice.datastore;

import chorestogetherapiservice.domain.Token;
import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.domain.UserEmail;
import chorestogetherapiservice.exception.datastore.ItemAlreadyExistException;
import chorestogetherapiservice.exception.datastore.NoItemFoundException;
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

/** Operation logics on User database. */
@Singleton
public class UserDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);
  private static final String BASE_TABLE_NAME = "user";
  private static final String GSI_NAME = "user_by_uid";
  private final DynamoDbTable<UserItem> table;

  private final DynamoDbIndex<UserItem> gsi;

  @SuppressWarnings("checkstyle:MissingJavadocMethod")
  @Inject
  public UserDao(DynamoDbEnhancedClient dynamoDbClient) {
    this.table = dynamoDbClient.table(BASE_TABLE_NAME,
        TableSchema.fromImmutableClass(UserItem.class));
    this.gsi = table.index(GSI_NAME);
  }

  /**
   * get user from User Table.
   *
   * @param  userEmail user's email to search.
   * @return                    If no result is found, NoItemFoundException occurs.
   *                            If found, Optional.of(User) is returned. */
  public UserItem get(UserEmail userEmail)
      throws DependencyFailureInternalException {
    Key key =  Key.builder().partitionValue(userEmail.getEmail()).build();
    UserItem userItem;
    try {
      userItem = table.getItem(key);
    } catch (Exception e) {
      LOGGER.error("DynamoDB exception occurred. Error message : " + e.getMessage());
      throw new DependencyFailureInternalException("DynamoDB exception occurred. Error message : "
          + e.getMessage(), e);
    }
    if (userItem == null) {
      throw new NoItemFoundException("User is not found at database");
    }
    return userItem;
  }

  /**
   * create new user at User Table.
   *
   * @param  user   new user to add
   */
  public void create(User user, Token token) {
    try {
      PutItemEnhancedRequest<UserItem> request
          = PutItemEnhancedRequest.builder(UserItem.class)
          .item(UserItem.of(user, token))
          .conditionExpression(
              Expression.builder()
                  .expression("attribute_not_exists(email)")
                  .build())
          .build();
      table.putItem(request);
    } catch (ConditionalCheckFailedException e) {
      throw new ItemAlreadyExistException("item already exists at database");
    } catch (Exception e) {
      LOGGER.error("DynamoDB exception occurred. Error message : " + e.getMessage());
      throw new DependencyFailureInternalException("DynamoDB exception occurred. Error message : "
          + e.getMessage(), e);
    }
  }
}
