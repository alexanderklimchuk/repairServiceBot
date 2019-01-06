package by.ts.management.rest;

import by.ts.management.rest.model.TaskNotification;
import by.ts.management.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskNotificationController {
  private final NotificationService service;

  @Autowired
  public TaskNotificationController(
      NotificationService notificationservice) {
    this.service = notificationservice;
  }

  @PostMapping("/notification")
  TaskNotification create(@RequestBody TaskNotification notification) {
    service.sendNotification(notification);
    return notification;
  }
}