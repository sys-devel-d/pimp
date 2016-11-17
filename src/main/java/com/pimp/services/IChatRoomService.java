package com.pimp.services;

import java.util.List;

import com.pimp.commons.mongo.IAbstractEntityService;
import com.pimp.domain.ChatRoom;


public interface IChatRoomService extends IAbstractEntityService<ChatRoom> {

  List<ChatRoom> getChatRoomsByParticipant(String userName);

}
