package si.fri.rso.kb6750.model3dcomments.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "model_3d_metadata")
@NamedQueries(value =
        {
                @NamedQuery(name = "Model3dMetadataEntity.getAll",
                        query = "SELECT md FROM Model3dMetadataEntity md")
        })

public class Model3dMetadataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "vertices")
    private Long vertices;
    @Column(name = "normals")
    private Long normals;
    @Column(name = "created")
    private Instant created;
    // @Column(name = "uri")
    // private String uri;
    @Column(name = "binaryArray")
    private byte[] binaryArray;
    @Column(name = "assetBundlebinaryArray")
    private byte[] assetBundleBinaryArray;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVertices() {
        return vertices;
    }

    public void setVertices(Long vertices) {
        this.vertices = vertices;
    }

    public Long getNormals() {
        return normals;
    }

    public void setNormals(Long normals) {
        this.normals = normals;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    /*
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }*/

    public byte[] getBinary() { return binaryArray; }

    public void setBinary(byte[] binary) { this.binaryArray = binary; }

    public byte[] getAssetBundleBinaryArray() {
        return assetBundleBinaryArray;
    }

    public void setAssetBundleBinaryArray(byte[] assetBundleBinaryArray) {
        this.assetBundleBinaryArray = assetBundleBinaryArray;
    }
}
