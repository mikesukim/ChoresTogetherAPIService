package chorestogetherapiservice.module;

import com.google.inject.Exposed;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

/**
 * Module for providing JwtBuilder and JwtParser types.
 * When creating both objects, key for HS256 algorithm should be injected.
 * Key size should be at least 32 bytes long for HS256 algorithm.
 * <a href="https://github.com/jwtk/jjwt#hmac-sha">more info</a>
 *  */
public class JwtModule extends PrivateModule {

  @Override
  protected void configure() {

  }

  @Provides
  @Singleton
  String key() {
    //TODO: pull a constant secret key from safe place.(e.x properties or AWS secret Manager)
    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    return Encoders.BASE64.encode(key.getEncoded());
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
