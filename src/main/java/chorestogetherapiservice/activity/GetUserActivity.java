package chorestogetherapiservice.activity;

import chorestogetherapiservice.domain.UserEmail;
import chorestogetherapiservice.exception.DependencyFailureInternalException;
import chorestogetherapiservice.exception.activity.DependencyFailureException;
import chorestogetherapiservice.exception.activity.ResourceNotFoundException;
import chorestogetherapiservice.exception.datastore.NoItemFoundException;
import chorestogetherapiservice.handler.ResponseHandler;
import chorestogetherapiservice.logic.GetUserLogic;
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
 * <br>
 * '@Path' is Jersey's annotation to indicate this class is jersey class resource.
 * Jersey class resource uses HK2 as a default HK2 as DI.
 * To use Guice as default DI, HK2-Guice-Bridge is required :
 * <a href="https://javaee.github.io/hk2/guice-bridge.html">HK2-Guice-Bridge link</a>
 * 'io.logz:guice-jersey' dependency configures the HK2-Guice-Bridge for enabling Guice.
 * When @Inject is used, Bridge will first look for instance from HK injector,
 * if instance not found, then Guice injector will be checked.
 *
 * <br>
 * '@RequestedScope' is a default life cycle for the jersey class resource (or JAR-RX)
 * <a href="https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/jaxrs-resources.html#d0e2692">
 *   jersey's life cycle description </a>
 * and using default life cycle instead of @Singleton is preferred since
 * 1) there's no high performance issue, 2) @Singleton can't be used with
 * passing @QueryParam as a field.
 *
 */
@Path("/users")
public class GetUserActivity {

  GetUserLogic getUserLogic;

  ResponseHandler responseHandler;

  @Inject
  public GetUserActivity(GetUserLogic getUserLogic, ResponseHandler responseHandler) {
    this.getUserLogic = getUserLogic;
    this.responseHandler = responseHandler;
  }

  /**
   * Check if user exists in service.
   *
   * @param  userEmailInput user's email as String.
   * @return                Response HTTP Response containing the result of user check as Json
   * */
  @GET
  @Path("/{userEmailInput}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@NotNull @NotEmpty @PathParam("userEmailInput") String userEmailInput)
      throws DependencyFailureInternalException {
    //TODO: decouple converting userEmailInput to UserEmail
    try {
      getUserLogic.getUser(new UserEmail(userEmailInput));
    } catch (DependencyFailureInternalException e) {
      throw new DependencyFailureException("Dependency failed while executing logic.", e);
    } catch (NoItemFoundException e) {
      throw new ResourceNotFoundException("User is not found.", e);
    }
    return responseHandler.generateSuccessResponse();
  }
}
