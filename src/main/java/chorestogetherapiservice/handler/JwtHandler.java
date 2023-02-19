package chorestogetherapiservice.handler;

import chorestogetherapiservice.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
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

  private final String JWT_HEADER_ISSUER = "ChoresTogetherAPIService";
  private final String JWT_HEADER_AUDIENCE = "ChoresTogether";

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
        .setSubject(user.getUid())
        .setAudience(JWT_HEADER_AUDIENCE)
        .setIssuedAt(Date.from(Instant.now()))
        .compact();
  }

  /**
   * parse JWT with jwtParser's secret key (see JwtModule).
   * If audience or issuer is not correct, then JwtException occurs.
   *
   * @return Token's subject, expected to be uid of User
   */
  public String parseJwt(String jwt) throws JwtException {
    Jws<Claims> parsedJwt;
    try {
      parsedJwt = jwtParser.parseClaimsJws(jwt);
      if (!JWT_HEADER_AUDIENCE.equals(parsedJwt.getBody().getAudience())
          || !JWT_HEADER_ISSUER.equals(parsedJwt.getBody().getIssuer())) {
        throw new JwtException("audience or issuer is not correct");
      }
    } catch (JwtException e) {
      throw new JwtException(e.getMessage(), e);
    }
    Optional<String> rawUid = Optional.ofNullable(parsedJwt.getBody().getSubject());
    if (rawUid.isEmpty()) {
      throw new JwtException("subject is missing");
    }
    return rawUid.get();
  }

}
