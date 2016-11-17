package com.pimp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pimp.commons.exceptions.EntityValidationException;
import com.pimp.commons.mongo.AbstractEntityService;
import com.pimp.commons.mongo.IKeyedObjectRepository;
import com.pimp.domain.ChatRoom;

/**
 *
 */

public class ChatRoomService extends AbstractEntityService<ChatRoom> implements IChatRoomService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ChatRoomService.class);

  public ChatRoomService(IKeyedObjectRepository<ChatRoom> entityRepository) {
    super(entityRepository, ChatRoom.class);
  }

  @Override
  public ChatRoom create() {
    return new ChatRoom();
  }

  @Override
  public void validate(ChatRoom chatRoom) throws EntityValidationException {
    if (get(chatRoom.getKey()) != null) {
      throw new EntityValidationException("An entity for the key exists already");
    } else if (chatRoom.getKey() == null) {
      throw new EntityValidationException("The key should not be null");
    } else {
      LOGGER.info("Entity successfully created!");
    }
  }

  // TODO: currently, a participant is set nowhere
  @Override
  public List<ChatRoom> getChatRoomsByParticipant(String userName) {
    return getEntitiesByKeys(getKeys())
      .stream()
      .filter(room -> room.getParticipants().contains(userName))
      .collect(Collectors.toList());
  }
}
