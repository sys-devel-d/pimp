package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.ChatRoom;
import com.pimp.domain.ChatRoomDocument;
import com.pimp.repositories.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatRoomService {

  private ChatRoomRepository chatRoomRepository;
  @Autowired
  private MongoOperations mongoOperations;

  @Autowired
  public ChatRoomService(ChatRoomRepository chatRoomRepository) {
    this.chatRoomRepository = chatRoomRepository;
  }

  public ChatRoomDocument createChatRoom(ChatRoom chatRoom) {
    String roomName = chatRoom.getRoomName();

    if (existsWithRoomName(roomName)) {
      throw new EntityAlreadyExistsException("ChatRoom already exists with roomName: " + roomName);
    }

    ChatRoomDocument chatRoomDocument = ChatRoomDocument.from(chatRoom);

    chatRoomRepository.save(chatRoomDocument);

    return chatRoomDocument;
  }

  private boolean existsWithRoomName(String roomName) {
    return chatRoomRepository.findByRoomName(roomName) != null;
  }

  public ChatRoom findByRoomName(String roomName) {
    ChatRoomDocument chatRoomDocument = chatRoomRepository.findByRoomName(roomName);

    if (chatRoomDocument == null) {
      throw new EntityNotFoundException("Unable to find chatRoom " + roomName);
    }

    return ChatRoom.from(chatRoomDocument);
  }

  public void save(ChatRoom chatRoom) {
    chatRoomRepository.save(ChatRoomDocument.from(chatRoom));
  }

  public ChatRoom createIfNotExists(String roomName) {
    ChatRoomDocument chatRoomDocument = chatRoomRepository.findByRoomName(roomName);

    if(chatRoomDocument == null) {
      chatRoomDocument = createChatRoom(
              new ChatRoom()
                  .setRoomName(roomName)
                  .setMessages(new ArrayList<>())
                  .setParticipants(new ArrayList<>())
      );
    }
    return ChatRoom.from(chatRoomDocument);
  }

  public List<ChatRoom> query(String query, List<String> queryParameter) {
    Query mongoQuery = new Query();
    Criteria criteria = new Criteria();
    Criteria[] criterias =
            queryParameter.stream().map(param -> Criteria.where(param).regex(query)).toArray(Criteria[]::new);
    criteria.orOperator(criterias);
    mongoQuery.addCriteria(criteria);
    return mongoOperations.find(mongoQuery, ChatRoomDocument.class).stream().map(ChatRoom::from).collect(Collectors.toList());
  }
}
