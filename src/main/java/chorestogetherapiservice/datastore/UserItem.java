package chorestogetherapiservice.datastore;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

// This bean will be used by DynamoDb-enhanced-client to be mapped with the User table.
// More info : https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/examples-dynamodb-enhanced.html

//TODO: @DynamoDbBean to @DynamoDbImmutable
@DynamoDbBean
public class UserItem {

    private String email;

    @DynamoDbPartitionKey
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
