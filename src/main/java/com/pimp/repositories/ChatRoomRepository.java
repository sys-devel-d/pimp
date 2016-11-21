package com.pimp.repositories;

import com.pimp.domain.ChatRoomDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoomDocument, String> {

  public ChatRoomDocument findByRoomName(String roomName);

  public ChatRoomDocument save(ChatRoomDocument chatRoomDocument);
}
