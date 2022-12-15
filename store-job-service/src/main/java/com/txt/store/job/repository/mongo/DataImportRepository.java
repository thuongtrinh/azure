package com.txt.store.job.repository.mongo;

import com.txt.store.job.entities.mongo.DataImport;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataImportRepository extends MongoRepository<DataImport, String> {

    DataImport findBy_id(ObjectId _id);

    List<DataImport> findByStatus(String status);
}
