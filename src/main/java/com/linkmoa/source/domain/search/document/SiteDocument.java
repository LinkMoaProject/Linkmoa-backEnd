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
@Document(indexName = Indices.SITE_INDEX)
public class SiteDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "nori")
    private String title; // 사이트 이름

    @Field(type = FieldType.Keyword)
    private String url;

    @Field(type = FieldType.Integer)
    private Integer orderIndex;

    @Field(type = FieldType.Boolean)
    private Boolean isFavorite;

    public SiteDocument(String id, String title, String url, Integer orderIndex, Boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.orderIndex = orderIndex;
        this.isFavorite = isFavorite;
    }
}
*/
