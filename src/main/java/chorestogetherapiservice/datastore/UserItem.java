package chorestogetherapiservice.datastore;

import chorestogetherapiservice.domain.Token;
import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.immutablesstyles.DynamoDbImmutableStyle;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.time.Instant;
import org.immutables.value.Value;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbImmutable;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

/** User Immutable class which is used by DynamoDb-enhanced-client to be mapped with the User table.
 *  More info :
 *  <a href="https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb-enhanced.html">dynamodb-enhanced-doc</a>
 *  <a href="https://github.com/aws/aws-sdk-java-v2/tree/master/services-custom/dynamodb-enhanced#working-with-immutable-data-classes">working-with-immutable-data-classes</a>*/

@Value.Immutable
@DynamoDbImmutableStyle
@DynamoDbImmutable(builder = UserItemBuilder.class)
public interface UserItem {

  @DynamoDbPartitionKey
  String getEmail();

  Instant getRegistrationDate();

  String getToken();

  @DynamoDbSecondaryPartitionKey(indexNames = "user_by_uid")
  String getUid();

  /**
   * Expressive factory methods fpr conversion of User type to UserItem.
   * <a href="https://immutables.github.io/immutable.html#expressive-factory-methods">doc</a>
   * */
  static UserItem of(User user, Token token) {
    return new UserItemBuilder()
        .email(user.getEmail())
        .registrationDate(Instant.now())
        .token(token.getToken())
        .uid(NanoIdUtils.randomNanoId())
        .build();
  }
}
