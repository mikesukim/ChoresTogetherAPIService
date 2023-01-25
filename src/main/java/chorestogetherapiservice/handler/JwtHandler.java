package chorestogetherapiservice.handler;

import chorestogetherapiservice.domain.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

/** Generate/Parse JWT.
 * JWT should be generated and returned only through CreateUser API.
 * JWT should be parsed and checked its integrity & authenticity for every API requests, except
 * CreateUser & LoginUser APIs
 * */
@Singleton
@SuppressWarnings({"checkstyle:AbbreviationAsWordInName", "checkstyle:MemberName"})
public class JwtHandler {

  private final String JWT_HEADER_ISSUER = "ChoresAPIService";
  private final String JWT_HEADER_AUDIENCE = "ChoresAPIService";

  JwtParser jwtParser;
  JwtBuilder jwtBuilder;

  @Inject
  public JwtHandler(JwtParser jwtParser, JwtBuilder jwtBuilder) {
    this.jwtParser = jwtParser;
    this.jwtBuilder = jwtBuilder;
  }

  /**
   * generate JWT signed with HS256 algorithm (see JwtModule).
   * JWT subject should be unique for each token, User type of string is used.
   * */
  public String generateJwt(User user) {
    return jwtBuilder
        .setIssuer(JWT_HEADER_ISSUER)
        .setSubject(user.toString())
        .setAudience(JWT_HEADER_AUDIENCE)
        .setIssuedAt(Date.from(Instant.now()))
        .compact();
  }

  /**
   * parse JWT.
   * on top of library's parsing through parseClaimsJws(),
   * Jwt subject is checked. If subject is not what is not expected,
   * parsing fails by raising  JwtException.
   * */
  public void parseJwt(Optional<String> jws, User user) throws JwtException {
    if (jws.isEmpty()) {
      throw new JwtException("empty jwt should not be passed");
    }
    try {
      String subject = jwtParser.parseClaimsJws(jws.get()).getBody().getSubject();
      if (!user.toString().equals(subject)) {
        throw new JwtException("JWT subject is not matched");
      }
    } catch (JwtException e) {
      throw new JwtException(e.getMessage(), e);
    }
  }

}
