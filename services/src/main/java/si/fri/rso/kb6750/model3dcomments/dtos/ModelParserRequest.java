package si.fri.rso.kb6750.model3dcomments.dtos;

public class ModelParserRequest {

    public ModelParserRequest() {
    }

    public ModelParserRequest(String title, String description, String binaryArrayString, String assetBundleBinaryArray) {
        this.title = title;
        this.description = description;
        this.binaryArrayString = binaryArrayString;
        this.assetBundleBinaryArray = assetBundleBinaryArray;
    }

    private String title;
    private String description;
    private String binaryArrayString;
    private String assetBundleBinaryArray;

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

    public String getBinaryArrayString() {
        return binaryArrayString;
    }

    public void setBinaryArrayString(String binaryArrayString) {
        this.binaryArrayString = binaryArrayString;
    }

    public String getAssetBundleBinaryArray() {
        return assetBundleBinaryArray;
    }

    public void setAssetBundleBinaryArray(String assetBundleBinaryArray) {
        this.assetBundleBinaryArray = assetBundleBinaryArray;
    }
}
