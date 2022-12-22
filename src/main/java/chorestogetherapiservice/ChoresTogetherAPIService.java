package chorestogetherapiservice;

import chorestogetherapiservice.module.AwsSdk2Module;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.JerseyServer;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vyarus.guice.validator.ValidationModule;

public class ChoresTogetherAPIService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChoresTogetherAPIService.class);
    public static void main(String[] args) throws Exception {
        JerseyConfiguration configuration = JerseyConfiguration.builder()
                .addPackage("chorestogetherapiservice")
                .addPort(8080)
                .build();

        Injector injector = Guice.createInjector(
                new JerseyModule(configuration),
                new ValidationModule(),
                new AwsSdk2Module()
        );
        JerseyServer server = injector.getInstance(JerseyServer.class);

        server.start();
        LOGGER.info("server started");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.stop();
                LOGGER.info("server stopped");
            } catch (Exception e) {
                LOGGER.error("exception occurred when stopping down server", e);
            }
        }));
    }
}
