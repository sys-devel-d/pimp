package com.pimp.commons.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Kevin Goy
 */
public interface IKeyedObjectRepository<T extends IKeyedObject> extends MongoRepository<T, String> {
}
