package si.fri.rso.kb6750.model3dcomments.services.clients;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import si.fri.rso.kb6750.model3dcomments.dtos.ModelParserRequest;

import javax.enterprise.context.Dependent;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.concurrent.CompletionStage;

@Path("")
@RegisterRestClient
@Dependent
public interface ModelParserApi {

    @POST
    CompletionStage<String> processImageAsynch(ModelParserRequest modelParserRequest);

}
