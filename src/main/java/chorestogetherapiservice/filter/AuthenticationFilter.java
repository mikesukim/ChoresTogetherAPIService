package chorestogetherapiservice.filter;

import chorestogetherapiservice.annotation.Authenticated;
import chorestogetherapiservice.exception.auth.NotAuthorizedException;
import chorestogetherapiservice.handler.JwtHandler;
import io.jsonwebtoken.JwtException;
import java.util.Optional;
import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

/**
 * This class is to authenticate token before letting request to reach its API entry point.
 * This class's filter method will be executed after Jersey successfully matched the resource.
 * For more detail info about filter, check ContainerRequestFilter doc.
 * <a href="https://eclipse-ee4j.github.io/jersey.github.io/apidocs/2.22/jersey/javax/ws/rs/container/ContainerRequestFilter.html">(ContainerRequestFilter doc)</a>
 * */
@Authenticated
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

  JwtHandler jwtHandler;

  @Inject
  public AuthenticationFilter(JwtHandler jwtHandler) {
    this.jwtHandler = jwtHandler;
  }

  @Override
  public void filter(ContainerRequestContext requestContext) {
    Optional<String> authHeader = Optional
        .ofNullable(requestContext.getHeaderString(HttpHeaders.AUTHORIZATION));
    if (authHeader.isEmpty()
        || !authHeader.get().startsWith("Bearer ")) {
      throw new NotAuthorizedException("Invalid Authorization header");
    }
    String rawToken = authHeader.get().split(" ")[1];
    String uid;
    try {
      uid = jwtHandler.parseJwt(rawToken);
    } catch (JwtException e) {
      throw new NotAuthorizedException(e.getMessage());
    }

    requestContext.setProperty("uid", uid);
  }
}
