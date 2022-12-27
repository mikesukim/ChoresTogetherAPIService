package chorestogetherapiservice.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/** Module to provide Properties type, which is a instance for reading values from cfg file. */
public class ConfigFileReaderModule extends AbstractModule {

  @Provides
  @Singleton
  Properties properties() throws IOException {
    // TODO: Make '*' character at cfg file to be recognized as a wildcard character.
    //  e.g. *.someProperty should be equal to a.someProperty or b.someProperty.
    Properties configFile;
    configFile = new java.util.Properties();
    configFile.load(new FileReader("ChoresTogetherAPIService.cfg"));
    return configFile;
  }
}
