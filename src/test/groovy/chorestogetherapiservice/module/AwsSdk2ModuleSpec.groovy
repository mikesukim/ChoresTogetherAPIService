package chorestogetherapiservice.module

import com.amazonaws.secretsmanager.caching.SecretCache
import software.amazon.awssdk.auth.credentials.AwsCredentials
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import spock.lang.Specification
import spock.lang.Subject

class AwsSdk2ModuleSpec extends Specification{

    @Subject
    def awsSdk2Module = new AwsSdk2Module()

    def "test success creation of DynamoDbClient"() {
        when:
        def endpointMock = "http://dynamodb-local:8000"
        def awsCredentialsProviderMock = Mock(AwsCredentialsProvider.class) {
            it.resolveCredentials() >> new AwsCredentials() {
                @Override
                String accessKeyId() {
                    return "accessKeyOnlyForDynamoDB"
                }
                @Override
                String secretAccessKey() {
                    return "secretAccessKeyOnlyForDynamoDB"
                }
            }
        }
        def result = awsSdk2Module.dynamoDbClient(endpointMock, awsCredentialsProviderMock)

        then:
        result instanceof DynamoDbClient
    }

    def "test success creation of DynamoDbEnhancedClient"() {
        when:
        def dynamodbClientMock = Mock(DynamoDbClient.class)
        def result = awsSdk2Module.dynamoDbEnhancedClient(dynamodbClientMock)

        then:
        result instanceof DynamoDbEnhancedClient
    }

    def "test success creation of SecretCache"() {
        when:
        def result = awsSdk2Module.secretCache()

        then:
        result instanceof SecretCache
    }
}
