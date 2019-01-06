package by.ts.management.service;

import by.ts.management.bot.AppContextHolder;
import by.ts.management.bot.Bot;
import by.ts.management.rest.model.TaskNotification;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  private final AppContextHolder ctxHolder;
  private Bot bot;

  @Autowired
  public NotificationService(AppContextHolder ctxHolder) {
    this.ctxHolder = ctxHolder;
  }

  public void sendNotification(TaskNotification notification) {
    bot.sendNotificationToAdmins(notification);
  }

  @PostConstruct
  public void init(){
    bot = ctxHolder.getBotInstance();
  }
}
