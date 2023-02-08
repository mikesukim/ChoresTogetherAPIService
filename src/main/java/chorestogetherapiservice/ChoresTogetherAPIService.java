package chorestogetherapiservice;

import chorestogetherapiservice.module.AwsSdk2Module;
import chorestogetherapiservice.module.JwtModule;
import chorestogetherapiservice.module.SocialLoginModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.JerseyServer;
import io.logz.guice.jersey.configuration.JerseyConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vyarus.guice.validator.ValidationModule;

/** Main method of ChoresTogetherAPIService.*/
@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
public class ChoresTogetherAPIService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ChoresTogetherAPIService.class);

  /**
   * Main method of ChoresTogetherAPIService.
   * it creates Guice Injector with given Modules and starts the server.
   * JerseyModule is for binding jersey to Jetty server, with the configuring jetty server to use
   * HK2-Guice bridge, so Guice can be used as DI for this project.
   */
  public static void main(String[] args) {

    JerseyConfiguration configuration = JerseyConfiguration.builder()
        .addPackage("chorestogetherapiservice")
        .addPort(8080)
        .build();

    Injector injector = Guice.createInjector(
        new JerseyModule(configuration),
        new ValidationModule(),
        new AwsSdk2Module(),
        new JwtModule(),
        new SocialLoginModule()
    );
    JerseyServer server = injector.getInstance(JerseyServer.class);

    try {
      server.start();

      Runtime.getRuntime().addShutdownHook(new Thread(() -> {
        try {
          server.stop();
          LOGGER.info("server stopped");
        } catch (Exception e) {
          LOGGER.error("exception occurred when stopping down server", e);
        }
      }));
    } catch (Exception e) {
      LOGGER.error("Error occurred while starting server. Error message: " + e.getMessage());
      return;
    }
    LOGGER.info("server started");
  }
}
