package com.pimp;

import com.pimp.controller.ChatController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactory;

import com.pimp.commons.exceptions.SpringBeanCreationException;
import com.pimp.domain.ChatRoom;
import com.pimp.repositories.ChatRoomRepository;
import com.pimp.repositories.IChatRoomRepository;
import com.pimp.services.ChatRoomService;
import com.pimp.services.IChatRoomService;

@SpringBootApplication
public class PimpRestApplication {

  @Bean
  public IChatRoomRepository roomRepository(MongoOperations mongoOperations) {
    MongoRepositoryFactory mongoRepositoryFactory = new MongoRepositoryFactory(mongoOperations);
    MongoEntityInformation<ChatRoom, String> entityInformation =
        mongoRepositoryFactory.getEntityInformation(ChatRoom.class);
    return mongoRepositoryFactory.getRepository(IChatRoomRepository.class,
        new ChatRoomRepository(entityInformation, mongoOperations));
  }

  @Bean
  public IChatRoomService chatRoomService(MongoOperations mongoOperations)
      throws SpringBeanCreationException {
    IChatRoomRepository repository = roomRepository(mongoOperations);
    return new ChatRoomService(repository);
  }

  @Bean
  public ChatController chatController(IChatRoomService chatRoomService) {
    return new ChatController(chatRoomService);
  }

  public static void main(String[] args) {
    SpringApplication.run(PimpRestApplication.class, args);
  }
}
