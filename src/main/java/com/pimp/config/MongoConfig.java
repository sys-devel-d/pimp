package com.pimp.config;

import com.pimp.commons.mongo.MongoFileStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

@Configuration
public class MongoConfig {

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
}
