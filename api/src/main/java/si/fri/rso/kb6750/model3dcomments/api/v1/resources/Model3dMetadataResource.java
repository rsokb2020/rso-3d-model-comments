package si.fri.rso.kb6750.model3dcomments.api.v1.resources;

import javax.annotation.PostConstruct;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.logging.Logger;
import com.kumuluz.ee.logs.cdi.Log;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.json.JSONObject;
import si.fri.rso.kb6750.model3dcomments.config.RestProperties;
import si.fri.rso.kb6750.model3dcomments.dtos.ModelParserRequest;
import si.fri.rso.kb6750.model3dcomments.lib.Model3dMetadata;
import si.fri.rso.kb6750.model3dcomments.services.beans.Model3dMetadataBean;
import si.fri.rso.kb6750.model3dcomments.services.clients.ModelParserApi;

@Log
@ApplicationScoped
@Path("/models3d")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Model3dMetadataResource {
    private Logger log = Logger.getLogger(Model3dMetadataResource.class.getName());

    @Inject
    private Model3dMetadataBean model3dMetadataBean;

    private ModelParserApi modelParserApi;

    @Inject
    private RestProperties restProperties;

    // @DiscoverService("image-processing-service")
    private String modelParserServiceUrl;

    @PostConstruct
    private void init() {
        modelParserServiceUrl = restProperties.getParserServiceIp();
        try {
            modelParserApi = RestClientBuilder
                    .newBuilder()
                    .baseUri(new URI(modelParserServiceUrl))
                    .build(ModelParserApi.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Context
    protected UriInfo uriInfo;
    @Log
    @GET
    public Response getModel3dMetadata() {

        List<Model3dMetadata> model3dMetadata = model3dMetadataBean.getModel3dMetadataFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(model3dMetadata).build();
    }
    /*
    @GET
    @Path("/info")
    public Response getModel3dMetadataInfo(@PathParam("model3dMetadataId") Integer model3dMetadataId) {

        JSONObject json = new JSONObject();

        json.put("clani", "['kb6750']");
        json.put("opis_projekta", "Aplikacija za nalaganje,shranjevanje in prikazovanje 3d modelov.");
        json.put("mikrostoritve", "['52.142.34.154:8080/v1/models3d','40.88.193.71:8080/v1/parser']");
        // json.put("uri", "This value should get removed soon.");
        json.put("github", "['https://github.com/rsokb2020/rso-3d-model-parser,'https://github.com/rsokb2020/rso-3d-model-catalog']");
        json.put("travis", "['https://github.com/rsokb2020/rso-3d-model-parser/actions','https://github.com/rsokb2020/rso-3d-model-catalog/actions']");
        json.put("dockerhub", "['https://hub.docker.com/repository/docker/klemiba/model-3d-parser'],['https://hub.docker.com/repository/docker/klemiba/model-3d-catalog']");

        return Response.status(Response.Status.OK).entity(json).build();
    }*/
    @Log
    @GET
    @Path("/{model3dMetadataId}")
    public Response getModel3dMetadata(@PathParam("model3dMetadataId") Integer model3dMetadataId) {

        Model3dMetadata model3dMetadata = model3dMetadataBean.getModel3dMetadata(model3dMetadataId);

        if (model3dMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(model3dMetadata.getNormals() == null || model3dMetadata.getVertices() == null){
            model3dMetadata = parseModel3dMetadataAsync(model3dMetadata, model3dMetadataId);
        }

        return Response.status(Response.Status.OK).entity(model3dMetadata).build();
    }

    @Log
    @GET
    @Path("/{model3dMetadataId}/sl")
    public Response getModel3dMetadataSlovenian(@PathParam("model3dMetadataId") Integer model3dMetadataId) throws IOException, InterruptedException {

        Model3dMetadata model3dMetadata = model3dMetadataBean.getModel3dMetadataSlovenian(model3dMetadataId);

        if (model3dMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(model3dMetadata).build();
    }

    @Log
    @GET
    @Path("/{model3dMetadataId}/assetBundle")
    public Response getModel3dMetadataAssetBundle(@PathParam("model3dMetadataId") Integer model3dMetadataId) {

        Model3dMetadata model3dMetadata = model3dMetadataBean.getModel3dMetadata(model3dMetadataId);

        if (model3dMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(Base64.decodeBase64(model3dMetadata.getAssetBundleBinaryArray())).build();
    }

    @Log
    @POST
    public Response createModel3dMetadata(Model3dMetadata model3dMetadata) {

        if ((model3dMetadata.getTitle() == null || model3dMetadata.getDescription() == null/* || model3dMetadata.getUri() == null*/)) {
            System.out.println(model3dMetadata.getTitle());
            System.out.println(model3dMetadata.getDescription());
            // System.out.println(model3dMetadata.getUri());
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            System.out.println(model3dMetadata.toString());
            model3dMetadata = model3dMetadataBean.createModel3dMetadata(model3dMetadata);
        }

        return Response.status(Response.Status.OK).entity(model3dMetadata).build();

    }
    @Log
    @PUT
    @Path("{model3dMetadataId}")
    public Response putModel3dMetadata(@PathParam("model3dMetadataId") Integer model3dMetadataId,
                                       Model3dMetadata model3dMetadata) {

        model3dMetadata = model3dMetadataBean.putModel3dMetadata(model3dMetadataId, model3dMetadata);

        if (model3dMetadata == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }
    @Log
    @DELETE
    @Path("{model3dMetadataId}")
    public Response deleteModel3dMetadata(@PathParam("model3dMetadataId") Integer model3dMetadataId) {

        boolean deleted = model3dMetadataBean.deleteModel3dMetadata(model3dMetadataId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    //@Timeout(value = 4, unit = ChronoUnit.SECONDS)
    //@CircuitBreaker(requestVolumeThreshold = 3)
    //@Fallback(fallbackMethod = "parseModelFallback")
    public Model3dMetadata parseModel3dMetadata(Model3dMetadata model3dMetadata){
        try {
            String url = restProperties.getParserServiceIp();
            System.out.println("This is the url: " + url);
            URL obj = new URL(url);

            JSONObject json = new JSONObject();

            json.put("title", model3dMetadata.getTitle());
            json.put("description", model3dMetadata.getDescription());
            String binaryArraydataStr = model3dMetadata.getBinary();
            String binaryArraydataAssetStr = model3dMetadata.getAssetBundleBinaryArray();
            json.put("binaryArrayString", binaryArraydataStr);
            json.put("assetBundleBinaryArray", binaryArraydataAssetStr);

            HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
            postConnection.setRequestMethod("POST");
            postConnection.setRequestProperty("Content-Type", "application/json");
            postConnection.setRequestProperty("request-chain", restProperties.getRequestChainHeader());
            postConnection.setDoOutput(true);

            OutputStream os = postConnection.getOutputStream();
            os.write(json.toString().getBytes());
            os.flush();
            os.close();
            int responseCode = postConnection.getResponseCode();
            System.out.println("POST Response Code :  " + responseCode);
            System.out.println("POST Response Message : " + postConnection.getResponseMessage());

            if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK ) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(postConnection.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in .readLine()) != null) {
                    response.append(inputLine);
                }

                JSONObject jsonResponse = new JSONObject(response.toString());
                // System.out.println("NUmber of vertices: " + response.toString());
                model3dMetadata.setVertices((long)Integer.parseInt(jsonResponse.get("numberOfVertices").toString()));
                model3dMetadata.setNormals((long)Integer.parseInt(jsonResponse.get("numberOfFaces").toString()));

                in.close();

                // System.out.println(response.toString());
            } else {
                System.out.println("POST NOT WORKED");
            }


        } catch (Exception ex) {
            System.out.println(ex);
            return model3dMetadata;
        }
        return model3dMetadata;
    }

    // Return existing model without parsing for extra info (we're asuming the parser does not work)
    public Model3dMetadata parseModelFallback(Model3dMetadata model3dMetadata){
        return model3dMetadata;
    }


    public Model3dMetadata parseModel3dMetadataAsync(Model3dMetadata model3dMetadata, Integer model3dMetadataId){
        // start image processing over async API
        CompletionStage<String> stringCompletionStage =
                modelParserApi.processImageAsynch(new ModelParserRequest(model3dMetadata.getTitle(),
                        model3dMetadata.getDescription(),
                        model3dMetadata.getBinary(),
                        model3dMetadata.getAssetBundleBinaryArray()));
        System.out.println("Before when complete");
        stringCompletionStage.whenComplete((s, throwable) -> {
            try{
                JSONObject jsonResponse = new JSONObject(s);
                // System.out.println("NUmber of vertices: " + response.toString());
                model3dMetadata.setVertices((long)Integer.parseInt(jsonResponse.get("numberOfVertices").toString()));
                model3dMetadata.setNormals((long)Integer.parseInt(jsonResponse.get("numberOfFaces").toString()));
                System.out.println("PUT Response: " );//+ model3dMetadataBean.putModel3dMetadata(model3dMetadataId, model3dMetadata));
            }catch(Exception e){
                System.out.println("Eecountered exception: " + e);
            }

        });
        stringCompletionStage.exceptionally(throwable -> {
            log.severe(throwable.getMessage());
            return throwable.getMessage();
        });

        return model3dMetadata;
    }
}
