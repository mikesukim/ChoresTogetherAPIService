package chorestogetherapiservice.datastore;

import chorestogetherapiservice.domain.UserEmail;
import chorestogetherapiservice.exception.datastore.NoItemFoundException;
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

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
   * Check if userEmail exists in database.
   *
   * @param  userEmail user's email to search.
   * @return                    If no result is found, Optional.Empty is returned.
   *                            If found, Optional.of(User) is returned. */
  @SuppressWarnings("checkstyle:JavadocParagraph")
  public UserItem get(UserEmail userEmail)
      throws DependencyFailureInternalException {
    UserItem key =  new UserItemBuilder().email(userEmail.getEmail()).build();
    UserItem userItem;
    try {
      userItem = table.getItem(key);
    } catch (Exception e) {
      LOGGER.error("DynamoDB exception occurred. Error message : " + e.getMessage());
      throw new DependencyFailureInternalException("DynamoDB exception occurred", e);
    }
    if (userItem == null) {
      throw new NoItemFoundException("User is not found at database");
    }
    return userItem;
  }
}
