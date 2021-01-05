package si.fri.rso.kb6750.model3dcomments.api.v1.resources;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

import si.fri.rso.kb6750.model3dcomments.lib.ImageMetadata;
import si.fri.rso.kb6750.model3dcomments.services.beans.ImageMetadataBean;

@ApplicationScoped
@Path("/images")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImageMetadataResource {

    private Logger log = Logger.getLogger(ImageMetadataResource.class.getName());

    @Inject
    private ImageMetadataBean imageMetadataBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getImageMetadata() {

        List<ImageMetadata> imageMetadata = imageMetadataBean.getImageMetadataFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(imageMetadata).build();
    }

    @GET
    @Path("/{imageMetadataId}")
    public Response getImageMetadata(@PathParam("imageMetadataId") Integer imageMetadataId) {

        ImageMetadata imageMetadata = imageMetadataBean.getImageMetadata(imageMetadataId);

        if (imageMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(imageMetadata).build();
    }

    @POST
    public Response createImageMetadata(ImageMetadata imageMetadata) {

        if ((imageMetadata.getTitle() == null || imageMetadata.getDescription() == null || imageMetadata.getUri() == null)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            imageMetadata = imageMetadataBean.createImageMetadata(imageMetadata);
        }

        return Response.status(Response.Status.OK).entity(imageMetadata).build();

    }

    @PUT
    @Path("{imageMetadataId}")
    public Response putImageMetadata(@PathParam("imageMetadataId") Integer imageMetadataId,
                                     ImageMetadata imageMetadata) {

        imageMetadata = imageMetadataBean.putImageMetadata(imageMetadataId, imageMetadata);

        if (imageMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }

    @DELETE
    @Path("{imageMetadataId}")
    public Response deleteImageMetadata(@PathParam("imageMetadataId") Integer imageMetadataId) {

        boolean deleted = imageMetadataBean.deleteImageMetadata(imageMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
