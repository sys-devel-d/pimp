package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.Notification;
import com.pimp.domain.NotificationChannel;
import com.pimp.domain.NotificationChannelDocument;
import com.pimp.repositories.NotificationChannelRepo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.Instant;

@Service
public class NotificationDispatcherService {

    private NotificationChannelRepo repo;

    public NotificationDispatcherService(NotificationChannelRepo notificationChannelRepo) {
        this.repo = notificationChannelRepo;
    }

    public Notification process(Notification notification) {
        if (notification.isAcknowledged()) {
            handleAcknowledge(notification);
        } else {
            notification = addNotification(notification);
        }
        return notification;
    }

    private Notification addNotification(Notification notification) {
        if (repo.exists(notification.getRoomId())) {
            NotificationChannelDocument document = (NotificationChannelDocument) repo.findOne(notification.getRoomId());
            notification.setCreationDate(Instant.now());
            notification.setKey(new ObjectId().toString());
            NotificationChannel channel = NotificationChannel.from(document);
            channel.addMessage(notification);
            repo.save(NotificationChannelDocument.from(channel));
        } else {
          throw new EntityNotFoundException("Could not find channel with id " + notification.getRoomId() + ".");
        }
        return notification;
    }

    private void handleAcknowledge(Notification notification) {
        switch (notification.getType()) {
            case EVENT_INVITATION:
                throw new NotImplementedException();
            case EVENT_UPDATE:
                throw new NotImplementedException();
            case NEW_MESSAGE:
                // nothing to do as of now
                break;
            default:
                throw new NotImplementedException();
        }
        updateAcked(notification);
    }

    private void updateAcked(Notification notification) {
        NotificationChannelDocument channel = repo.findByRoomName(notification.getRoomId());
        channel.getMessages().stream()
                .filter(message -> message.getKey().equals(notification.getKey()))
                .map(message -> ((Notification) message).setAcknowledged(notification.isAcknowledged()));
        repo.save(channel);
    }

    public NotificationChannel find(String channelName) {
      if (repo.exists(channelName)) {
        NotificationChannelDocument document = (NotificationChannelDocument) repo.findOne(channelName);
        return NotificationChannel.from(document);
      } else {
        throw new EntityNotFoundException("A channel with the name " + channelName + " does not exist.");
      }
    }

    public NotificationChannelDocument create(NotificationChannel channel) {
      if (repo.exists(channel.getRoomName())) {
        throw new EntityAlreadyExistsException("A channel with the name " + channel.getRoomName() + " already exists.");
      }

      return repo.save(NotificationChannelDocument.from(channel));
    }

}
