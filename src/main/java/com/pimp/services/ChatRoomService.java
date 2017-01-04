package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.ChatRoom;
import com.pimp.domain.ChatRoomDocument;
import com.pimp.domain.Message;
import com.pimp.domain.User;
import com.pimp.repositories.ChatRoomRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
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

  public List<ChatRoom> findUsersRooms(User user) {
    Query mongoQuery = new Query();
    mongoQuery.addCriteria(Criteria.where("participants.userName").in(user.getUserName()));
    return mongoOperations.find(mongoQuery, ChatRoomDocument.class).stream().map(ChatRoom::from).collect(Collectors.toList());
  }

  public void insertMessageIntoRoom(Message message) {
    mongoOperations.updateFirst(
      Query.query(Criteria.where("_id").is(message.getRoomId())),
      new Update().push("messages", message),
      ChatRoomDocument.class
    );
  }

  public ChatRoom initializeRoom(List<User> users, String roomType, HashMap<String, String> displayNames) throws NoSuchAlgorithmException {
    String roomName;
    if(roomType.equals(ChatRoom.ROOM_TYPE_PRIVATE)) {
      roomName = md5FromUsernames(users);
    }
    else {
      roomName = ObjectId.get().toString();
    }

    if(!existsWithRoomName(roomName)) {
      ChatRoomDocument chatRoomDocument = createChatRoom(
              new ChatRoom()
                      .setRoomName(roomName)
                      .setParticipants(users)
                      .setDisplayNames(displayNames)
                      .setMessages(new ArrayList<>())
                      .setRoomType(roomType)
      );
      return ChatRoom.from(chatRoomDocument);
    }

    return null;
  }

  private String md5FromUsernames(List<User> users) throws NoSuchAlgorithmException {
    List<String> sortedNames = users.stream().map(User::getUserName).sorted().collect(Collectors.toList());
    byte[] hash = MessageDigest.getInstance("MD5").digest(String.join("_", sortedNames).getBytes());
    StringBuilder sb = new StringBuilder(2*hash.length);
    for(byte b : hash) {
      sb.append(String.format("%02x", b&0xff));
    }
    return sb.toString();
  }
}
