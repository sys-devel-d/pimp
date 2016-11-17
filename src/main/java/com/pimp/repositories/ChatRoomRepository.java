package com.pimp.repositories;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;

import com.pimp.domain.ChatRoom;

/**
 * Created by julianfink on 17/11/16.
 */
public class ChatRoomRepository extends CustomKeyedObjectRepository<ChatRoom> implements IChatRoomRepository {

  public ChatRoomRepository(MongoEntityInformation<ChatRoom, String> metadata, MongoOperations mongoOperations) {
    super(metadata, mongoOperations);
  }
}
