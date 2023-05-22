package com.txt.mongoredis.repositories.mongo;

import com.txt.mongoredis.entities.mongo.BaseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<T extends BaseEntity, ID> extends MongoRepository<T, ID>, QuerydslPredicateExecutor<T> {
}
