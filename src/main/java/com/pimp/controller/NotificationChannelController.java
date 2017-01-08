package com.pimp.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pimp.domain.Notification;
import com.pimp.domain.NotificationChannel;
import com.pimp.services.NotificationDispatcherService;

@RequestMapping("/notification")
@RestController
public class NotificationChannelController  {

  private NotificationDispatcherService service;

  @Autowired
  public NotificationChannelController(NotificationDispatcherService service) {
    this.service = service;
  }

  @RequestMapping(path = "/user/{user}", method = RequestMethod.POST)
  public void init(@PathVariable String user) {
    NotificationChannel channel = (NotificationChannel) new NotificationChannel()
      .setRoomName(user);

    service.create(channel);
  }

  @RequestMapping(path = "/user/{user}", method = RequestMethod.GET)
  public String find(@PathVariable String user) {
    return service.find(user).getRoomName();
  }

  @RequestMapping(path = "messages/{user}")
  public List<Notification> getNotificationsForUser(@PathVariable String user) {
    List<Notification> notifications =
      (List<Notification>)(List<?>) service.find(user).getMessages();
    return notifications
      .stream()
      .filter(notification -> !notification.isAcknowledged())
      .collect(Collectors.toList());
  }

  @RequestMapping(path = "/acknowledge", method = RequestMethod.POST)
  public void acknowledgeNotification(@RequestBody Notification notification) {
    service.updateAcked(notification);
  }
}
