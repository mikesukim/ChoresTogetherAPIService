package chorestogetherapiservice.datastore;

import chorestogetherapiservice.domain.ImmutableUserEmail;
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

/** Operation logics on User database. */
@Singleton
public class UserDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);
  private static final String TABLE_NAME = "User";
  private final DynamoDbTable<UserItem> table;

  @Inject
  public UserDao(DynamoDbEnhancedClient dynamoDbClient) {
    this.table = dynamoDbClient.table(TABLE_NAME, TableSchema.fromBean(UserItem.class));
  }

  /**
   * Check if userEmail exists in database.
   *
   * @param  immutableUserEmail user's email to search.
   * @return                    If no result is found, Optional.Empty is returned.
   *                            If found, Optional.of(User) is returned. */
  @SuppressWarnings("checkstyle:JavadocParagraph")
  public Optional<UserItem> get(ImmutableUserEmail immutableUserEmail)
      throws DependencyFailureInternalException {
    Optional<UserItem> resultItem;
    Key key = Key.builder()
        .partitionValue(immutableUserEmail.getEmail())
        .build();
    try {
      resultItem = Optional.ofNullable(
          table.getItem(
              (GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key)));
    } catch (Exception e) {
      LOGGER.error("DynamoDB exception occurred. Error message : " + e.getMessage());
      throw new DependencyFailureInternalException("DynamoDB exception occurred", e);
    }
    return resultItem;
  }
}
