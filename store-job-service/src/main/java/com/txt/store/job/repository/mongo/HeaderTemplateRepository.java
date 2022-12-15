package com.txt.store.job.repository.mongo;

import com.txt.store.job.entities.mongo.HeaderTemplate;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeaderTemplateRepository extends MongoRepository<HeaderTemplate, String> {

    HeaderTemplate findBy_id(ObjectId _id);

    List<HeaderTemplate> findByCode(String code);

    List<HeaderTemplate> findByName(String name);
}
