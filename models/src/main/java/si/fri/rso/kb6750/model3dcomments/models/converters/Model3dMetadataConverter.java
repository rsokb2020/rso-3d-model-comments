package si.fri.rso.kb6750.model3dcomments.models.converters;

import org.apache.commons.codec.binary.Base64;
import si.fri.rso.kb6750.model3dcomments.lib.Model3dMetadata;
import si.fri.rso.kb6750.model3dcomments.models.entities.Model3dMetadataEntity;

public class Model3dMetadataConverter {

    public static Model3dMetadata toDto(Model3dMetadataEntity entity) {

        Model3dMetadata dto = new Model3dMetadata();
        dto.setModelId(entity.getId());
        dto.setCreated(entity.getCreated());
        dto.setDescription(entity.getDescription());
        dto.setTitle(entity.getTitle());
        dto.setVertices(entity.getVertices());
        dto.setNormals(entity.getNormals());
        // dto.setUri(entity.getUri());
        dto.setBinary(Base64.encodeBase64String(entity.getBinary()));
        dto.setAssetBundleBinaryArray(Base64.encodeBase64String(entity.getAssetBundleBinaryArray()));
        return dto;
    }

    public static Model3dMetadataEntity toEntity(Model3dMetadata dto) {

        Model3dMetadataEntity entity = new Model3dMetadataEntity();
        entity.setCreated(dto.getCreated());
        entity.setDescription(dto.getDescription());
        entity.setTitle(dto.getTitle());
        entity.setVertices(dto.getVertices());
        entity.setNormals(dto.getNormals());
        // entity.setUri(dto.getUri());
        entity.setBinary(Base64.decodeBase64(dto.getBinary()));
        entity.setAssetBundleBinaryArray(Base64.decodeBase64(dto.getAssetBundleBinaryArray()));
        return entity;
    }
}
