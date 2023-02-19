package com.txt.mongoredis.entities.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public abstract class BaseEntity<T> {

    @Id
    private T id;
}
