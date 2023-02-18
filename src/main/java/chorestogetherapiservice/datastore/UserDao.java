package chorestogetherapiservice.datastore;

import chorestogetherapiservice.constant.Constants;
import chorestogetherapiservice.domain.Token;
import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.domain.UserEmail;
import chorestogetherapiservice.exception.datastore.ItemAlreadyExistException;
import chorestogetherapiservice.exception.datastore.NoItemFoundException;
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

/** Operation logics on User database. */
@Singleton
public class UserDao {
  private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);
  private static final String BASE_TABLE_NAME = Constants.USER_BASE_TABLE_NAME;
  private static final String GSI_NAME = Constants.USER_GSI_NAME;
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
   * get user from User Table's GSI.
   *
   * @param  userEmail user's email to search.
   * @return           If no result is found, NoItemFoundException occurs.
   *                   If multiple userItems are found, error should be occurred since
   *                   email should be unique per user. */
  public UserItem get(UserEmail userEmail)
      throws DependencyFailureInternalException {

    // build query
    // see how dynamodb query works
    // https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Query.Pagination.html
    Key key =  Key.builder().partitionValue(userEmail.getEmail()).build();
    QueryConditional queryConditional = QueryConditional.keyEqualTo(key);
    QueryEnhancedRequest queryEnhancedRequest = QueryEnhancedRequest
        .builder()
        .queryConditional(queryConditional)
        .limit(2) // two items with same email address is already an error.
        .scanIndexForward(false)
        .build();

    // initiate first query.
    // subsequent queries will be initiated once the latest page is accessed, one by one.
    SdkIterable<Page<UserItem>> result;
    try {
      result = gsi.query(queryEnhancedRequest);
    } catch (Exception e) {
      LOGGER.error("DynamoDB exception occurred. Error message : " + e.getMessage());
      throw new DependencyFailureInternalException("DynamoDB exception occurred. Error message : "
          + e.getMessage());
    }

    List<UserItem> userItems = convertQueryResultToList(result, 1);
    if (userItems.size() == 0) {
      throw new NoItemFoundException("User is not found at database.");
    }
    if (userItems.size() > 1) {
      throw new ItemAlreadyExistException("multiple users with same email exist.");
    }

    return userItems.get(0);
  }

  private <T> List<T> convertQueryResultToList(
      SdkIterable<Page<T>> pages, int subsequentQueriesLimit) {
    ArrayList<T> itemList = new ArrayList<>();
    // setting stream size limit to one prevents calling next subsequent query.
    pages.stream().limit(subsequentQueriesLimit).forEach(
        page -> itemList.addAll(page.items())
    );
    return itemList;
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
                  //TODO : switch to uid
                  .expression("attribute_not_exists(email)")
                  .build())
          .build();
      table.putItem(request);
    } catch (ConditionalCheckFailedException e) {
      throw new ItemAlreadyExistException("item already exists at database");
    } catch (Exception e) {
      LOGGER.error("DynamoDB exception occurred. Error message : " + e.getMessage());
      throw new DependencyFailureInternalException("DynamoDB exception occurred. Error message : "
          + e.getMessage());
    }
  }
}
