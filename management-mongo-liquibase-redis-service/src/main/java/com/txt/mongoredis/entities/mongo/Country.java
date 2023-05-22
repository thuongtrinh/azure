package com.txt.mongoredis.entities.mongo;

import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@QueryEntity
@Document("country")
@Data
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Country extends BaseEntity<String> {

    private String code;

    private String name;
}
