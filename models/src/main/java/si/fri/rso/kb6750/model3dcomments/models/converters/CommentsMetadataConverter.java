package si.fri.rso.kb6750.model3dcomments.models.converters;

import org.apache.commons.codec.binary.Base64;
import si.fri.rso.kb6750.model3dcomments.lib.CommentsMetadata;
import si.fri.rso.kb6750.model3dcomments.lib.Model3dMetadata;
import si.fri.rso.kb6750.model3dcomments.models.entities.CommentsMetadataEntity;
import si.fri.rso.kb6750.model3dcomments.models.entities.Model3dMetadataEntity;

public class CommentsMetadataConverter {

    public static CommentsMetadata toDto(CommentsMetadataEntity entity) {

        CommentsMetadata dto = new CommentsMetadata();
        dto.setCommentId(entity.getId());
        dto.setModelId(entity.getModelId());
        dto.setCreated(entity.getCreated());
        dto.setContent(entity.getContent());
        dto.setUsername(entity.getUsername());
        return dto;
    }

    public static CommentsMetadataEntity toEntity(CommentsMetadata dto) {

        CommentsMetadataEntity entity = new CommentsMetadataEntity();
        entity.setCreated(dto.getCreated());
        entity.setId(dto.getCommentId());
        entity.setModelId(dto.getModelId());
        entity.setContent(dto.getContent());
        entity.setUsername(dto.getUsername());
        return entity;
    }
}
