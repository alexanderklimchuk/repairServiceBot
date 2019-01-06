package by.losika.ts;

import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;


@Component
public class AppInitializer
{
    private static final Logger logger = LogManager.getLogger(AppInitializer.class);

    @Value("${bot.username}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.ts.contactInfo}")
    private String contactInfo;

    @Value("${bot.ts.greetingText}")
    private String greetingText;

    private final GoogleFormSubmitter submitter;

    private AppInitializer(GoogleFormSubmitter submitter){
        this.submitter = submitter;
    }

    @PostConstruct
    public void initApp(){
        logger.info("Bot has been started");
        ApiContextInitializer.init();
        Bot bot = new Bot();
        configureBot(bot);
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
            telegramBotsApi.registerBot(bot);
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
    }
}