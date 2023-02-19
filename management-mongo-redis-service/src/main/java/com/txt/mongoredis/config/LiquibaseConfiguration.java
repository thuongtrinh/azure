package com.txt.mongoredis.config;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.ext.mongodb.database.MongoLiquibaseDatabase;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LiquibaseConfiguration implements InitializingBean {

    @Value("${spring.liquibase.uri}")
    private String uri;

    @Value("${spring.liquibase.change-log}")
    private String resources;

    @Override
    public void afterPropertiesSet() throws Exception {
        MongoLiquibaseDatabase openDatabase = (MongoLiquibaseDatabase) DatabaseFactory.getInstance()
                .openDatabase(uri, null, null, null, null, null);

        Liquibase liquibase = new Liquibase(resources, new ClassLoaderResourceAccessor(), openDatabase);
        liquibase.update(new Contexts());
    }
}
