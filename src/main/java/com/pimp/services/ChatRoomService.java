package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.ChatRoom;
import com.pimp.domain.ChatRoomDocument;
import com.pimp.domain.User;
import com.pimp.repositories.ChatRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

  /**
   * Fetches a room by roomName from database.
   * If it doesn't exists then it creates one and saves it to database.
   */
  public ChatRoom getExistingOrCreate(String roomName) {
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

  public List<ChatRoom> findUsersRooms(User user) {
    Query mongoQuery = new Query();
    mongoQuery.addCriteria(Criteria.where("participants.userName").in(user.getUserName()));
    return mongoOperations.find(mongoQuery, ChatRoomDocument.class).stream().map(ChatRoom::from).collect(Collectors.toList());
  }

  public ChatRoom initUniqueRoom(User invitee, User invited, String roomType) throws NoSuchAlgorithmException {
    String uniqueRoomName = md5FromUsernames(invitee, invited);

    if(!existsWithRoomName(uniqueRoomName)) {
      ChatRoomDocument chatRoomDocument = createChatRoom(
              new ChatRoom()
                .setRoomName(uniqueRoomName)
                .setParticipants(Arrays.asList(invitee, invited))
                .setMessages(new ArrayList<>())
                .setRoomType(roomType)
      );
      return ChatRoom.from(chatRoomDocument);
    }

    return null;
  }

  private String md5FromUsernames(User invitee, User invited) throws NoSuchAlgorithmException {
    List<String> sortedNames = Arrays.asList(invitee.getUserName(), invited.getUserName());
    Collections.sort(sortedNames);
    byte[] hash = MessageDigest.getInstance("MD5").digest(String.join("_", sortedNames).getBytes());
    StringBuilder sb = new StringBuilder(2*hash.length);
    for(byte b : hash) {
      sb.append(String.format("%02x", b&0xff));
    }
    return sb.toString();
  }
}
