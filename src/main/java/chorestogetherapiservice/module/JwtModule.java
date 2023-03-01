package chorestogetherapiservice.module;

import chorestogetherapiservice.util.EnvValReader;
import com.amazonaws.secretsmanager.caching.SecretCache;
import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Module for providing JwtBuilder and JwtParser types.
 * When creating both objects, key for HS256 algorithm should be injected.
 * Key size should be at least 32 bytes long for HS256 algorithm.
 * <a href="https://github.com/jwtk/jjwt#hmac-sha">more info</a>
 *  */
public class JwtModule extends PrivateModule {

  private static final String LOCAL_JWT_SECRET_KEY =
      "q5416deaICrNXfeCxdRErU/qGxyF7D0vUqfvxHaipAt98nSVuS5/EJKLxcKerILy";
  private static final String JWT_SECRET_KEY_SECRET_ID = "ChoresTogetherApiServiceJwtSecretKey";

  @Override
  protected void configure() {

  }

  @Provides
  @Singleton
  String key(SecretCache secretCache) {
    String stage = EnvValReader.getStage();
    // TODO: test coverage. PowerMockito is required.
    if (stage.equals("local")) {
      return LOCAL_JWT_SECRET_KEY;
    }
    return secretCache.getSecretString(JWT_SECRET_KEY_SECRET_ID);
  }

  @Provides
  @Singleton
  @Exposed
  JwtBuilder jwtBuilder(String secretKey) {
    return Jwts
        .builder()
        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)));
  }

  @Provides
  @Singleton
  @Exposed
  JwtParser jwtParser(String secretKey) {
    return Jwts
        .parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
        .build();
  }
}
