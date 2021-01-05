package si.fri.rso.kb6750.model3dcomments.lib;

import java.time.Instant;

public class Model3dMetadata {
    private Integer modelID;
    private String title;
    private String description;
    private Long vertices;
    private Long normals;
    private Instant created;
    // private String uri;
    private String binaryArray;
    private String assetBundleBinaryArray;


    public Integer getModelId() {
        return modelID;
    }

    public void setModelId(Integer modelID) {
        this.modelID = modelID;
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

    public String getBinary() {
        return binaryArray;
    }

    public void setBinary(String binaryArray) {
        this.binaryArray = binaryArray;
    }

    public String getAssetBundleBinaryArray() {
        return assetBundleBinaryArray;
    }

    public void setAssetBundleBinaryArray(String assetBundleBinaryArray) {
        this.assetBundleBinaryArray = assetBundleBinaryArray;
    }
}
