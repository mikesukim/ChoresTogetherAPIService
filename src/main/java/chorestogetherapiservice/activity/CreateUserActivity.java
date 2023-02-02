package chorestogetherapiservice.activity;

import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.logic.CreateUserLogic;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * THIS API SHOULD BE ONLY CALLED FOR DEVELOPMENT PURPOSE.
 * CreateUser API entry class.
 */
@Path("/users")
public class CreateUserActivity {
  CreateUserLogic createUserLogic;

  @Inject
  public CreateUserActivity(CreateUserLogic createUserLogic) {
    this.createUserLogic = createUserLogic;
  }

  /**
   * create user.
   *
   * @param  user Immutable user type. Request's Json body is converted by
   *              JAX-RS's MessageBodyReader(and implemented jersey-media-json-jackson) into
   *              User Immutable type
   * @return      HTTP Response containing the result of user check as Json
   * */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createUser(@NotNull User user) {
    return createUserLogic.createUser(user);
  }
}
