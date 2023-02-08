package chorestogetherapiservice.module;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * Module for providing objects for validating ID token, eventually for logging into our service.
 */
public class SocialLoginModule extends PrivateModule {

  @Override
  protected void configure() {

  }

  // TODO: move client IDs to resource file
  private static final String GOOGLE_OAUTH2_PLAYGROUND_ID =
      "407408718192.apps.googleusercontent.com";
  private static final String GOOGLE_CLIENT_ID =
      "862022442251-qbkf0j8qvnj8ketip74l1944hmj4cr6v.apps.googleusercontent.com";

  @Provides
  @Singleton
  @Exposed
  GoogleIdTokenVerifier googleIdTokenVerifier(NetHttpTransport netHttpTransport,
                                              JsonFactory jsonFactory) {
    return new GoogleIdTokenVerifier.Builder(netHttpTransport, jsonFactory)
        .setAudience(Arrays.asList(GOOGLE_CLIENT_ID, GOOGLE_OAUTH2_PLAYGROUND_ID))
        .build();
  }

  @Provides
  @Singleton
  NetHttpTransport netHttpTransport() throws GeneralSecurityException, IOException {
    // https://cloud.google.com/java/docs/reference/google-http-client/latest/com.google.api.client.http.HttpTransport
    return GoogleNetHttpTransport.newTrustedTransport();
  }

  @Provides
  @Singleton
  JsonFactory jsonFactory() {
    // https://cloud.google.com/java/docs/reference/google-http-client/latest/com.google.api.client.json.jackson2
    return new GsonFactory();
  }
}
