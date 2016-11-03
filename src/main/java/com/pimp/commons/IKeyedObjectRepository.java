package com.pimp.commons;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author Kevin Goy
 */
public interface IKeyedObjectRepository<T extends IKeyedObject> extends MongoRepository<T, String> {
}
