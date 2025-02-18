/*
package com.linkmoa.source.domain.search.document;


import com.linkmoa.source.domain.search.constant.Indices;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor
@Document(indexName = Indices.DIRECTORY_INDEX)
public class DirectoryDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title;

    @Field(type = FieldType.Integer)
    private Integer orderIndex;

    @Field(type = FieldType.Boolean)
    private Boolean isFavorite;

    public DirectoryDocument(String id, String title, Integer orderIndex, Boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.orderIndex = orderIndex;
        this.isFavorite = isFavorite;
    }
}
*/
