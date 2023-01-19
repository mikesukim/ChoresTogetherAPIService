package chorestogetherapiservice.activity;

import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.exception.datastore.ItemAlreadyExistException;
import chorestogetherapiservice.exception.dependency.DependencyFailureInternalException;
import chorestogetherapiservice.handler.ResponseHandler;
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
 * CreateUser API entry class.
 * For explanation about Jersey's annotations & resource life cycle,
 * read GetUserActivity's javadoc.
 */
@Path("/users")
public class CreateUserActivity {
  ResponseHandler responseHandler;

  CreateUserLogic createUserLogic;

  @Inject
  public CreateUserActivity(ResponseHandler responseHandler, CreateUserLogic createUserLogic) {
    this.responseHandler = responseHandler;
    this.createUserLogic = createUserLogic;
  }

  /**
   * create user.
   *
   * @param  user Immutable user type. Request's Json body is converted by
   *              JAX-RS's MessageBodyReader(and implemented jersey-media-json-jackson) into
   *              User Immutable type
   * */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createUser(@NotNull User user) {
    try {
      createUserLogic.createUser(user);
    } catch (DependencyFailureInternalException e) {
      return responseHandler.generateFailResponseWith(e.getMessage());
    } catch (ItemAlreadyExistException e) {
      return responseHandler.generateBadRequestErrorResponseWith(e.getMessage());
    }
    return responseHandler.generateSuccessResponse();
  }
}
