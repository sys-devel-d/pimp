package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.ChatRoomDocument;
import com.pimp.domain.Notification;
import com.pimp.domain.NotificationChannel;
import com.pimp.domain.NotificationChannelDocument;
import com.pimp.repositories.NotificationChannelRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class NotificationDispatcherService {

    private NotificationChannelRepo repo;
    @Autowired
    private MongoOperations mongoOperations;

    public NotificationDispatcherService(NotificationChannelRepo notificationChannelRepo) {
        this.repo = notificationChannelRepo;
    }

    public Notification process(Notification notification) {
        return addNotification(notification);
    }

    private Notification addNotification(Notification notification) {
        if (repo.exists(notification.getReceivingUser())) {
            notification.setCreationDate(Instant.now());
            notification.setKey(new ObjectId().toString());
            mongoOperations.updateFirst(
              Query.query(Criteria.where("_id").is(notification.getRoomId())),
              new Update().push("messages", notification),
              ChatRoomDocument.class
            );
        } else {
            throw new EntityNotFoundException("Could not find channel with id " + notification.getReceivingUser() + ".");
        }
        return notification;
    }

    public void updateAcked(Notification notification) {
        Query query = Query.query(new Criteria().andOperator(
          Criteria.where("_id").is(notification.getReceivingUser()),
          Criteria.where("messages").elemMatch(Criteria.where("key").is(notification.getKey()))
        ));
        mongoOperations.updateFirst(
          query,
          new Update().set("messages.$.acknowledged", notification.isAcknowledged()),
          ChatRoomDocument.class
        );
    }

    public NotificationChannel find(String channelName) {
        if (repo.exists(channelName)) {
            NotificationChannelDocument document = (NotificationChannelDocument) repo.findOne(channelName);
            return NotificationChannel.from(document);
        }
        throw new EntityNotFoundException("A channel with the name " + channelName + " does not exist.");
    }

    public NotificationChannelDocument create(NotificationChannel channel) {
        if (repo.exists(channel.getRoomName())) {
            throw new EntityAlreadyExistsException("A channel with the name " + channel.getRoomName() + " already exists.");
        }

        return repo.save(NotificationChannelDocument.from(channel));
    }

}
