package chorestogetherapiservice.datastore;

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
  private static final String TABLE_NAME = "User";
  private final DynamoDbTable<UserItem> table;

  @Inject
  public UserDao(DynamoDbEnhancedClient dynamoDbClient) {
    this.table = dynamoDbClient.table(TABLE_NAME, TableSchema.fromImmutableClass(UserItem.class));
  }

  /**
   * get user from User Table.
   *
   * @param  userEmail user's email to search.
   * @return                    If no result is found, Optional.Empty is returned.
   *                            If found, Optional.of(User) is returned. */
  @SuppressWarnings("checkstyle:JavadocParagraph")
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
  public void create(User user) {
    try {
      PutItemEnhancedRequest<UserItem> request
          = PutItemEnhancedRequest.builder(UserItem.class)
          .item(UserItem.of(user))
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
