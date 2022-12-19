package chorestogetherapiservice.activity;

import chorestogetherapiservice.domain.UserEmail;
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
    public Response getUser(@NotNull @NotEmpty @PathParam("userEmailInput") String userEmailInput) {
        // TODO : convert userEmailInput to UserEmail
        getUserLogic.getUser(new UserEmail());
        return Response.status(200).entity("getUser is called, email : " + userEmailInput).build();
    }
}
