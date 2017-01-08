package com.pimp.services;

import java.time.Instant;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.Notification;
import com.pimp.domain.NotificationChannel;
import com.pimp.domain.NotificationChannelDocument;
import com.pimp.repositories.NotificationChannelRepo;

@Service
public class NotificationDispatcherService {

    private NotificationChannelRepo repo;

    public NotificationDispatcherService(NotificationChannelRepo notificationChannelRepo) {
        this.repo = notificationChannelRepo;
    }

    public Notification process(Notification notification) {
        return addNotification(notification);
    }

    private Notification addNotification(Notification notification) {
        if (repo.exists(notification.getReceivingUser())) {
            NotificationChannelDocument document = (NotificationChannelDocument) repo.findOne(notification.getReceivingUser());
            notification.setCreationDate(Instant.now());
            notification.setKey(new ObjectId().toString());
            NotificationChannel channel = NotificationChannel.from(document);
            channel.addMessage(notification);
            repo.save(NotificationChannelDocument.from(channel));
        } else {
            throw new EntityNotFoundException("Could not find channel with id " + notification.getReceivingUser() + ".");
        }
        return notification;
    }

    public void updateAcked(Notification notification) {
        NotificationChannelDocument channel = (NotificationChannelDocument) repo.findOne(notification.getReceivingUser());
        channel.getMessages().forEach(message -> {
            if (message.getKey().equals(notification.getKey())) {
                ((Notification) message).setAcknowledged(true);
            }
        });
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
