package chorestogetherapiservice.activity;

import chorestogetherapiservice.domain.User;
import chorestogetherapiservice.domain.UserEmail;
import chorestogetherapiservice.exception.DependencyFailureInternalException;
import chorestogetherapiservice.exception.datastore.NoItemFoundException;
import chorestogetherapiservice.exception.activity.DependencyFailureException;
import chorestogetherapiservice.exception.activity.ResourceNotFoundException;
import chorestogetherapiservice.logic.GetUserLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/users")
public class GetUserActivity {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChoresActivity.class);

    GetUserLogic getUserLogic;

    @Inject
    public GetUserActivity(GetUserLogic getUserLogic) {
        this.getUserLogic = getUserLogic;
    }

    @GET
    @Path("/{userEmailInput}")
    public Response getUser(@NotNull @NotEmpty @PathParam("userEmailInput") String userEmailInput) throws DependencyFailureInternalException {
        // TODO : decouple converting userEmailInput to UserEmail
        User user;
        try {
            user = getUserLogic.getUser(new UserEmail(userEmailInput));
        } catch (DependencyFailureInternalException e) {
            throw new DependencyFailureException("Dependency failed while executing logic.", e);
        } catch (NoItemFoundException e) {
            throw new ResourceNotFoundException("User is not found.", e);
        }
        return Response.status(200).entity("user is found. email : " + user.getEmail()).build();
    }
}
