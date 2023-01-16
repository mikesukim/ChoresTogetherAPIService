package chorestogetherapiservice.activity;

import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.handler.ResponseHandler;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * CreateUser API entry class.
 * For explanation about Jersey's annotations & resource life cycle,
 * read GetUserActivity's javadoc.
 */
@Path("/users")
public class CreateUserActivity {
  ResponseHandler responseHandler;

  @Inject
  public CreateUserActivity(ResponseHandler responseHandler) {
    this.responseHandler = responseHandler;
  }

  /**
   * Check if user exists in service.
   *
   * @param  user Immutable user type. Request's Json body is converted by
   *              JAX-RS's MessageBodyReader(and implemented jersey-media-json-jackson) into
   *              User Immutable type
   * @return      Response HTTP Response containing the result of user check as Json
   * */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createUser(@NotNull User user) {
    //TODO : Implement the logic
    return responseHandler.generateFailResponseWith("not implemented yet");
  }
}
