package si.fri.rso.kb6750.model3dcomments.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "comments_metadata")
@NamedQueries(value =
        {
                @NamedQuery(name = "CommentsMetadataEntity.getAll",
                        query = "SELECT md FROM CommentsMetadataEntity md")
        })

public class CommentsMetadataEntity {
    /*
    * private Integer commentId;
    private String content;
    private Instant created;
    private String username;
    * */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer commentId;

    @Column(name = "modelId")
    private Integer modelId;
    @Column(name = "content")
    private String content;
    @Column(name = "username")
    private String username;
    @Column(name = "created")
    private Instant created;


    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public Integer getId() {
        return commentId;
    }

    public void setId(Integer id) {
        this.commentId = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }
}
