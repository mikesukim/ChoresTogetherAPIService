package chorestogetherapiservice.module;

import chorestogetherapiservice.util.EnvValReader;
import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import java.net.URI;
import java.util.Properties;
import javax.inject.Singleton;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Module to provide AWS related objects.
 * It extends PrivateModule,
 * since few object only needed to be exposed within this module. (e.x. dynamoDbClient)
 */
public class AwsSdk2Module extends PrivateModule {

  private static final String DYNAMODB_ENDPOINT = "dynamodb.endpoint";

  @Override
  protected void configure() {
    install(new ConfigFileReaderModule());
  }

  @Provides
  @Singleton
  String dynamodbEndpoint(Properties properties) {
    // TODO: test coverage. PowerMockito is required.
    String stage = EnvValReader.getStage();
    String region = EnvValReader.getRegion();
    return properties.getProperty(stage + "." + region + "." + DYNAMODB_ENDPOINT);
  }

  @Provides
  @Singleton
  AwsCredentialsProvider awsCredentialsProvider() {
    // TODO: test coverage. PowerMockito is required.
    String stage = EnvValReader.getStage();
    AwsCredentialsProvider awsCredentialsProvider;
    if (stage.equals("local")) {
      awsCredentialsProvider = StaticCredentialsProvider.create(new AwsCredentials() {
        @Override
        public String accessKeyId() {
          return "accessKeyOnlyForDynamoDB";
        }

        @Override
        public String secretAccessKey() {
          return "secretAccessKeyOnlyForDynamoDB";
        }
      });
    } else {
      // 5th order of DefaultCredentialsProvider chain, the ContainerCredentialsProvider
      // should pull credential from its ECS task IAM role.
      // https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/credentials.html#credentials-chain
      awsCredentialsProvider = DefaultCredentialsProvider.create();
    }
    return awsCredentialsProvider;
  }

  @Provides
  @Singleton
  DynamoDbClient dynamoDbClient(String dynamoDbEndPoint,
                                AwsCredentialsProvider awsCredentialsProvider) {
    return DynamoDbClient.builder()
        .credentialsProvider(awsCredentialsProvider)
        //TODO: add retries
        .endpointOverride(URI.create(dynamoDbEndPoint))
        // The region is meaningless for local DynamoDb but required for client builder validation
        .region(DefaultAwsRegionProviderChain.builder().build().getRegion())
        .build();
  }

  @Provides
  @Singleton
  @Exposed
  DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
    return DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoDbClient)
        .build();
  }
}
