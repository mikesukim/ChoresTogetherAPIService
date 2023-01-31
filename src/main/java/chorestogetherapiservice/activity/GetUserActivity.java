package chorestogetherapiservice.activity;

import chorestogetherapiservice.domain.ImmutableUserEmail;
import chorestogetherapiservice.exception.datastore.NoItemFoundException;
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException;
import chorestogetherapiservice.logic.GetUserLogic;
import java.util.regex.PatternSyntaxException;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * GetUser API entry class.
 *
 * <p>
 *   Following paragraphs are to explain basic of Jersey's/jax-rs annotations.
 *   '@Path' is Jersey's annotation to indicate this class is jersey class resource.
 *   Jersey class resource uses HK2 as a default HK2 as DI.
 *   To use Guice as default DI, HK2-Guice-Bridge is required :
 *   <a href="https://javaee.github.io/hk2/guice-bridge.html">HK2-Guice-Bridge link</a>
 *   'io.logz:guice-jersey' dependency configures the HK2-Guice-Bridge for enabling Guice.
 *   When @Inject is used, Bridge will first look for instance from HK injector,
 *   if instance not found, then Guice injector will be checked.
 * </p>
 *
 * <p>
 *   '@RequestedScope' is a default life cycle for the jersey class resource (or JAR-RX).
 *   <a href="https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/jaxrs-resources.html#d0e2692">
 *   (jersey's life cycle description) </a>
 *   Instead of '@Singleton', default life cycle is used following
 *   <a href="https://stackoverflow.com/questions/30409895/jax-rs-resource-lifecycle-performance-impact">this discussion</a>.
 * </p>
 */
@Path("/users")
public class GetUserActivity {

  GetUserLogic getUserLogic;

  @Inject
  public GetUserActivity(GetUserLogic getUserLogic) {
    this.getUserLogic = getUserLogic;
  }

  /**
   * Check if user exists in service.
   *
   * @param  userEmailInput user's email as String.
   * @return                HTTP Response containing the result of user check as Json
   * */
  @GET
  @Path("/{userEmailInput}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@NotNull @NotEmpty @PathParam("userEmailInput") String userEmailInput)
      throws PatternSyntaxException, DependencyFailureInternalException, NoItemFoundException {
    ImmutableUserEmail immutableUserEmail =
        ImmutableUserEmail.builder()
            .email(userEmailInput)
            .build();
    return getUserLogic.getUser(immutableUserEmail);
  }
}
