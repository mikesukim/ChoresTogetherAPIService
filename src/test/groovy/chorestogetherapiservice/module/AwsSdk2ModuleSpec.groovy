package chorestogetherapiservice.module

import com.google.inject.Guice
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import spock.lang.Specification
import spock.lang.Subject

class AwsSdk2ModuleSpec extends Specification{

    @Subject
    def awsSdk2Module = new AwsSdk2Module()

    def injector =  Guice.createInjector(awsSdk2Module)

    def "test dynamoDbEnhancedClient injection"() {
        when:
        def result = injector.getInstance(DynamoDbEnhancedClient.class)

        then:
        result instanceof DynamoDbEnhancedClient
    }
}
