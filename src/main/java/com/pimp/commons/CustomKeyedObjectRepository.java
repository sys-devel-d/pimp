package com.pimp.commons;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

/**
 * @author Kevin Goy
 */
public class CustomKeyedObjectRepository<T extends IKeyedObject> extends SimpleMongoRepository<T, String>
    implements IKeyedObjectRepository<T> {

  protected final MongoEntityInformation<T, String> metadata;

  protected MongoOperations mongoOperations;

  public CustomKeyedObjectRepository(MongoEntityInformation<T, String> metadata, MongoOperations mongoOperations) {
    super(metadata, mongoOperations);

    this.metadata = metadata;
    this.mongoOperations = mongoOperations;
  }
}
