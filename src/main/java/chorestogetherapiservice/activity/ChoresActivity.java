package chorestogetherapiservice.activity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/*
@Path is Jersey's annotation to indicate this class is jersey class resource.
Jersey class resource uses HK2 as a default HK2 as DI.
To use Guice as default DI, HK2-Guice-Bridge is required : https://javaee.github.io/hk2/guice-bridge.html
'io.logz:guice-jersey' dependency configures the HK2-Guice-Bridge for enabling Guice.
When @Inject is used, Bridge will first look for instance from HK injector,
if instance not found, then Guice injector will be checked.
 */

/*
This @Singleton is for the jersey class resource's scope,
since the default scope is @RequestedScope.
https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/jaxrs-resources.html#d0e2692
 */
@Singleton
@Path("/chores")
public class ChoresActivity {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChoresActivity.class);

    @GET
    public String getChore(@NotNull @NotEmpty @QueryParam("rawChoreId") String rawChoreId) {
        LOGGER.debug("createChore initiated");
        return "hello chore";
    }

    @POST
    public String createChore() {
        LOGGER.debug("postChore initiated");
        return "hello chore";
    }
}
