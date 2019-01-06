package by.ts.management.bot;

import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AppContextHolder {

  private static final Logger logger = LogManager.getLogger(AppContextHolder.class);

  @Value("${bot.username}")
  private String botUsername;

  @Value("${bot.token}")
  private String botToken;

  @Value("${bot.ts.contactInfo}")
  private String contactInfo;

  @Value("${bot.ts.greetingText}")
  private String greetingText;

  @Value("${bot.ts.adminChatId}")
  private String adminChatId;

  private Bot botInstance;

  @Autowired
  private GoogleFormSubmitter submitter;

  @PostConstruct
  public void initApp() {
    logger.info("Bot has been started");
    ApiContextInitializer.init();
    botInstance = new Bot();
    configureBot(botInstance);

    try {
      TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
      telegramBotsApi.registerBot(botInstance);
    } catch (TelegramApiRequestException e) {
      logger.error("Unexpected error has been occurred", e);
    }
  }

  private void configureBot(Bot bot) {
    bot.setBotToken(botToken);
    bot.setBotUsername(botUsername);
    bot.setContactInfo(contactInfo);
    bot.setFormSubmitter(submitter);
    bot.setGreetingText(greetingText);
    bot.setAdminChatId(adminChatId);
  }

  public Bot getBotInstance() {
    return botInstance;
  }

  public void setBotInstance(Bot botInstance) {
    this.botInstance = botInstance;
  }
}