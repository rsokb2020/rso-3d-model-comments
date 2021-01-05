package si.fri.rso.kb6750.model3dcomments.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import org.json.JSONObject;
import si.fri.rso.kb6750.model3dcomments.config.RestProperties;
import si.fri.rso.kb6750.model3dcomments.lib.CommentsMetadata;
import si.fri.rso.kb6750.model3dcomments.models.converters.CommentsMetadataConverter;
import si.fri.rso.kb6750.model3dcomments.models.entities.CommentsMetadataEntity;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class CommentsMetadataBean {
    private Logger log = Logger.getLogger(CommentsMetadataBean.class.getName());

    @Inject
    private EntityManager em;
    @Inject
    private RestProperties restProperties;

    public List<CommentsMetadata> getCommentsMetadata() {

        TypedQuery<CommentsMetadataEntity> query = em.createNamedQuery(
                "CommentsMetadataEntity.getAll", CommentsMetadataEntity.class);

        List<CommentsMetadataEntity> resultList = query.getResultList();

        return resultList.stream().map(CommentsMetadataConverter::toDto).collect(Collectors.toList());

    }
    //TODO: Preveri kako dobiš vse komentarje ki imajo ID Modela
    public List<CommentsMetadata> getCommentsMetadataFilter(UriInfo uriInfo, Integer modelId) {

        QueryParameters queryParameters = QueryParameters.query("SELECT * FROM CommentsMetadataEntity WHERE modelId =" + modelId).defaultOffset(0)
                .build();

        List<CommentsMetadata> commentsMetadataList = JPAUtils.queryEntities(em, CommentsMetadataEntity.class, queryParameters).stream()
               .map(CommentsMetadataConverter::toDto).collect(Collectors.toList());

        return commentsMetadataList;
    }

    public CommentsMetadata getCommentsMetadata(Integer id) {

        CommentsMetadataEntity commentsMetadataEntity = em.find(CommentsMetadataEntity.class, id);

        if (commentsMetadataEntity == null) {
            throw new NotFoundException();
        }

        CommentsMetadata commentsMetadata = CommentsMetadataConverter.toDto(commentsMetadataEntity);


        return commentsMetadata;
    }

    public CommentsMetadata getCommentsMetadataSlovenian(Integer modelId, Integer commentId) throws IOException, InterruptedException {

        CommentsMetadataEntity commentsMetadataEntity = em.find(CommentsMetadataEntity.class, commentId);

        if (commentsMetadataEntity == null) {
            throw new NotFoundException();
        }

        CommentsMetadata commentsMetadata = CommentsMetadataConverter.toDto(commentsMetadataEntity);

        String q = "q=" + commentsMetadata.getContent();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://google-translate1.p.rapidapi.com/language/translate/v2"))
                .header("content-type", "application/x-www-form-urlencoded")
                .header("accept-encoding", "application/gzip")
                .header("x-rapidapi-key", "214b6f647cmshf4546d2f616ce97p1d05b1jsnc2a9f0b5b749")
                .header("x-rapidapi-host", "google-translate1.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString(q+"&source=en&target=sl"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        String translated = response.body();
        JSONObject json = new JSONObject(translated);
        String content = json.getJSONObject("data").getJSONArray("translations").getJSONObject(0).getString("translatedText");
        System.out.println(content);
        commentsMetadata.setContent(content);
        return commentsMetadata;
    }

    public CommentsMetadata createCommentsMetadata(CommentsMetadata commentsMetadata) {

        CommentsMetadataEntity commentsMetadataEntity = CommentsMetadataConverter.toEntity(commentsMetadata);

        try {
            beginTx();
            em.persist(commentsMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (commentsMetadataEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return CommentsMetadataConverter.toDto(commentsMetadataEntity);
    }

    public CommentsMetadata putCommentsMetadata(Integer id, CommentsMetadata commentsMetadata) {

        CommentsMetadataEntity c = em.find(CommentsMetadataEntity.class, id);

        if (c == null) {
            return null;
        }

        CommentsMetadataEntity updatedCommentsMetadataEntity = CommentsMetadataConverter.toEntity(commentsMetadata);

        try {
            beginTx();
            updatedCommentsMetadataEntity.setId(c.getId());
            updatedCommentsMetadataEntity = em.merge(updatedCommentsMetadataEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return CommentsMetadataConverter.toDto(updatedCommentsMetadataEntity);
    }

    public boolean deleteCommentsMetadata(Integer id) {

        CommentsMetadataEntity commentsMetadata = em.find(CommentsMetadataEntity.class, id);

        if (commentsMetadata != null) {
            try {
                beginTx();
                em.remove(commentsMetadata);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
