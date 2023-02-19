package com.txt.mongoredis.repositories.mongo;

import com.txt.mongoredis.entities.mongo.UserInfo;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo, ObjectId>, QuerydslPredicateExecutor<UserInfo> {
}
