//package com.txt.mongoredis.config;
//
//import org.springframework.boot.autoconfigure.mongo.MongoProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.mongodb.MongoDatabaseFactory;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
//
//@Configuration
//public class MongoConfiguration {
//
//    //region MasterMongo Collection Config.
//    @Primary
//    @ConfigurationProperties(prefix = "spring.data.mongodb")
//    public MongoProperties getMasterMongoProperties() {
//        return new MongoProperties();
//    }
//
//    @Primary
//    @Bean
//    public MongoDatabaseFactory masterMongoFactory(MongoProperties mongo) {
//        return new SimpleMongoClientDatabaseFactory(mongo.getUri());
//    }
//
//    @Primary
//    public MongoTemplate masterMongoTemplate() {
//        return new MongoTemplate(masterMongoFactory(getMasterMongoProperties()));
//    }
//    //endregion
//
//}
