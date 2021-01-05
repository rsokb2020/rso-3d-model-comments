package si.fri.rso.kb6750.model3dcomments.api.v1.resources;

import com.kumuluz.ee.logs.cdi.Log;
import org.apache.commons.codec.binary.Base64;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.json.JSONObject;
import si.fri.rso.kb6750.model3dcomments.config.RestProperties;
import si.fri.rso.kb6750.model3dcomments.dtos.ModelParserRequest;
import si.fri.rso.kb6750.model3dcomments.lib.CommentsMetadata;
import si.fri.rso.kb6750.model3dcomments.lib.Model3dMetadata;
import si.fri.rso.kb6750.model3dcomments.services.beans.CommentsMetadataBean;
import si.fri.rso.kb6750.model3dcomments.services.beans.Model3dMetadataBean;
import si.fri.rso.kb6750.model3dcomments.services.clients.ModelParserApi;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;

@Log
@ApplicationScoped
@Path("/comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentsMetadataResource {
    private Logger log = Logger.getLogger(CommentsMetadataResource.class.getName());

    @Inject
    private CommentsMetadataBean commentsMetadataBean;

    @Inject
    private RestProperties restProperties;

    @Context
    protected UriInfo uriInfo;
    @Log
    @GET
    @Path("/{model3dMetadataId}")
    public Response getCommentsMetadata(@PathParam("model3dMetadataId") Integer model3dMetadataId) {
        System.out.println("Recieved call!");
        List<CommentsMetadata> model3dMetadata = commentsMetadataBean.getCommentsMetadataFilter(uriInfo, model3dMetadataId);

        return Response.status(Response.Status.OK).entity(model3dMetadata).build();
    }
    /*
    @Log
    @GET
    @Path("/{model3dMetadataId}")
    public Response getCommentsMetadata(@PathParam("model3dMetadataId") Integer model3dMetadataId) {

        CommentsMetadata commentsMetadata = commentsMetadataBean.getCommentsMetadata(model3dMetadataId);

        if (commentsMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(commentsMetadata).build();
    }*/

    @Log
    @GET
    @Path("/{model3dMetadataId}/{commentId}")
    public Response getCommentWithIdMetadata(@PathParam("model3dMetadataId") Integer model3dMetadataId,
                                             @PathParam("commentId") Integer commentId) {

        CommentsMetadata commentsMetadata = commentsMetadataBean.getCommentsMetadata(commentId);

        if (commentsMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(commentsMetadata).build();
    }

    @Log
    @GET
    @Path("/{model3dMetadataId}/{commentId}/sl")
    public Response getCommentsMetadataSlovenian(@PathParam("model3dMetadataId") Integer model3dMetadataId,
                                                 @PathParam("commentId") Integer commentId) throws IOException, InterruptedException {

        CommentsMetadata commentsMetadata = commentsMetadataBean.getCommentsMetadataSlovenian(model3dMetadataId, commentId);

        if (commentsMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(commentsMetadata).build();
    }


    @Log
    @POST
    public Response createCommentsMetadata(CommentsMetadata commentsMetadata) {

        if ((commentsMetadata.getContent() == null)){
            System.out.println(commentsMetadata.getContent());
            // System.out.println(model3dMetadata.getUri());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            System.out.println(commentsMetadata.toString());
            commentsMetadata = commentsMetadataBean.createCommentsMetadata(commentsMetadata);
        }

        return Response.status(Response.Status.OK).entity(commentsMetadata).build();

    }
    @Log
    @PUT
    @Path("{commentId}")
    public Response putCommentsMetadata(@PathParam("commentId") Integer commentId,
                                       CommentsMetadata commentsMetadata) {

        commentsMetadata = commentsMetadataBean.putCommentsMetadata(commentId, commentsMetadata);

        if (commentsMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }
    @Log
    @DELETE
    @Path("{commentId}")
    public Response deleteCommentsMetadata(@PathParam("commentId") Integer commentId) {

        boolean deleted = commentsMetadataBean.deleteCommentsMetadata(commentId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
