package chorestogetherapiservice.module;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import java.net.URI;
import java.util.Properties;
import javax.inject.Singleton;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.providers.DefaultAwsRegionProviderChain;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

/**
 * Module to provide AWS related objects.
 * It extends PrivateModule,
 * since few object only needed to be exposed within this module. (e.x. dynamoDbClient)
 */
public class AwsSdk2Module extends PrivateModule {

  private static final String DYNAMODB_ENDPOINT = "local.us-west-2.dynamodb.endpoint.table.user";

  @Override
  protected void configure() {
    install(new ConfigFileReaderModule());
  }

  @Provides
  @Singleton
  DynamoDbClient dynamoDbClient(Properties properties) {
    return DynamoDbClient.builder()
        //TODO: get stage and region from ENVIRONMENT variables
        //TODO: add retries
        .endpointOverride(URI.create(properties.getProperty(DYNAMODB_ENDPOINT)))
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
