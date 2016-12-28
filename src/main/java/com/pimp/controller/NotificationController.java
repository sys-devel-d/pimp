package com.pimp.controller;

import com.pimp.domain.Notification;
import com.pimp.services.NotificationDispatcherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

  private NotificationDispatcherService service;

  @Autowired
  public NotificationController(NotificationDispatcherService service) {
    this.service = service;
  }

  @MessageMapping("/broker/notifications/{user}")
  @SendTo("/notifications/{user}")
  public Notification handleNotification(Notification notification) {
    return service.process(notification);
  }

}
