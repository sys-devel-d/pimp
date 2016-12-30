package com.pimp.controller;

import com.pimp.domain.Message;
import com.pimp.domain.NotificationChannel;
import com.pimp.services.NotificationDispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/notification")
@RestController
public class NotificationChannelController  {

  private NotificationDispatcherService service;

  @Autowired
  public NotificationChannelController(NotificationDispatcherService service) {
    this.service = service;
  }

  @RequestMapping(path = "/{user}", method = RequestMethod.POST)
  public void init(@PathVariable String user) {
    NotificationChannel channel = (NotificationChannel) new NotificationChannel()
      .setRoomName(user);

    service.create(channel);
  }

  @RequestMapping(path = "/{user}", method = RequestMethod.GET)
  public String find(@PathVariable String user) {
    return service.find(user).getRoomName();
  }

  @RequestMapping(path = "messages/{user}")
  public List<? extends Message> getNotificationsForUser(@PathVariable String user) {
    return service.find(user).getMessages();
  }
}
