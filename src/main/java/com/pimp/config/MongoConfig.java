package com.pimp.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.pimp.commons.mongo.MongoFileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = {"com.pimp.repositories"})
public class MongoConfig extends AbstractMongoConfiguration{

    @Value(("${db.host}"))
    private String dbHost;

    @Value("${db.name}")
    private String dbName;

    @Bean
    public MongoFileStorage mongoFileStorage(GridFsTemplate gridFsTemplate) {
        return new MongoFileStorage(gridFsTemplate);
    }

    @Bean(name = "mappingConverter")
    public MongoConverter mongoConverter(MongoDbFactory mongoDbFactory) {
        MappingMongoConverter mappingMongoConverter =
                new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), new MongoMappingContext());
        return mappingMongoConverter;
    }

    @Override
    protected String getDatabaseName() {
        return dbName;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(dbHost);
    }
}
