package com.morris.opensquare.models.documents;

import com.morris.opensquare.models.FileType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.BsonBinary;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

@Getter
@Setter
@Builder
@ToString
@org.springframework.data.mongodb.core.mapping.Document("documents")
public class Document {

    @BsonId
    private ObjectId id;
    private String fileName;
    private long fileSize;
    private BsonBinary binary;
    private FileType fileType;
}
