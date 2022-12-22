package chorestogetherapiservice.module

import com.google.inject.Guice
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import spock.lang.Specification
import spock.lang.Subject

class ConfigFileReaderModuleSpec extends Specification {

    @Subject
    def configFileReaderModule = new ConfigFileReaderModule()

    def injector =  Guice.createInjector(configFileReaderModule)

    def "test Properties injection"() {
        when:
        def result = injector.getInstance(Properties.class)

        then:
        result instanceof Properties
    }
}
