package chorestogetherapiservice.datastore;

import chorestogetherapiservice.domain.UserEmail;
import chorestogetherapiservice.exception.DependencyFailureInternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class UserDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);
    private final static String TABLE_NAME = "User";
    private final DynamoDbTable<UserItem> table;

    @Inject
    public UserDao(DynamoDbEnhancedClient dynamoDbClient) {
        this.table = dynamoDbClient.table(TABLE_NAME, TableSchema.fromBean(UserItem.class));
    }

    public Optional<UserItem> get(UserEmail userEmail) throws DependencyFailureInternalException {
        Optional<UserItem> resultItem;
        Key key = Key.builder()
                .partitionValue(userEmail.getEmail())
                .build();
        try {
            resultItem = Optional.ofNullable(
                    table.getItem((GetItemEnhancedRequest.Builder requestBuilder) -> requestBuilder.key(key)));
        } catch (Exception e) {
            LOGGER.error("DynamoDB exception occurred. Error message : " + e.getMessage());
            throw new DependencyFailureInternalException("DynamoDB exception occurred", e);
        }
        return resultItem;
    }
}
