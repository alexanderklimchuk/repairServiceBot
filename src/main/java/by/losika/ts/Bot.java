package by.losika.ts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.*;

public class Bot extends TelegramLongPollingBot {

    private String botUsername;
    private String botToken;
    private String contactInfo;
    private String greetingText;

    private GoogleFormSubmitter formSubmitter;

    private static final Logger logger = LogManager.getLogger(Bot.class);
    public static final String CURRENT_QUESTION = "current_question";

    HashMap<String, HashMap<String, String>> dialogs = new HashMap<>();
    final List<String> sequenceOfTaskQuestions = Arrays.asList("Квартира", "Имя", "Телефон", "Описание проблемы");
    final String stopWord = "Отмена";

    public void onUpdateReceived(Update update) {
        synchronized (this){
            final String chatId = update.getMessage().getChatId().toString();
            String message = update.getMessage().getText();
            if(stopWord.equalsIgnoreCase(message)){
                logger.info(new ParameterizedMessage("Cancel button was pressed for chatId: {}", chatId));
                dialogs.put(chatId, null);
                showMainMenu(chatId, message);
            }

            Map<String, String> progress = dialogs.get(chatId);

            if(progress != null) {
                String currentQuestion = progress.get(CURRENT_QUESTION);
                progress.put(currentQuestion, message);

                logger.info(new ParameterizedMessage("Response was received: {} from chatId: {}", message, chatId));

                int currentQuestionIdx = sequenceOfTaskQuestions.indexOf(currentQuestion);
                if(currentQuestionIdx < sequenceOfTaskQuestions.size() - 1) {
                    progress.put(CURRENT_QUESTION, sequenceOfTaskQuestions.get(sequenceOfTaskQuestions.indexOf(currentQuestion) + 1));
                    showNewTaskMenu(chatId);

                } else {
                    formSubmitter.submitGoogleForm(progress);
                    dialogs.put(chatId, null);
                    sendSpecifiedText(chatId, "Заявка принята, спасибо!");
                    showMainMenu(chatId, message);
                }
            } else {
                showMainMenu(chatId, message);
            }
        }
    }


    private void showMainMenu(String chatId, String message) {
        if(message == null) {
            String youAreNotPoliteWithMe = "Вы общаетесь со мной без уважения.";
            sendSpecifiedText(chatId, youAreNotPoliteWithMe);
            return;
        }
        switch(message){
            case "Контакты ТС": sendSpecifiedText(chatId, contactInfo);
                break;
            case "Новая заяка": showNewTaskMenu(chatId);
                break;
            default: sendSpecifiedText(chatId, greetingText);
        }
    }

    private void showNewTaskMenu(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        addButtonsToTheMessage(sendMessage, new String[]{"Отмена"});
        HashMap<String, String> taskProgress = dialogs.get(chatId);

        if(taskProgress == null){
            taskProgress = new HashMap<>();
            taskProgress.put(CURRENT_QUESTION, sequenceOfTaskQuestions.get(0));
            dialogs.put(chatId, taskProgress);
        }
        sendMessage.setText(taskProgress.get(CURRENT_QUESTION));
        try {
            sendMessage(sendMessage);
            logger.info(new ParameterizedMessage("Text: \"{}\" was sent to chatId: {}", taskProgress.get(CURRENT_QUESTION), chatId));
        } catch (TelegramApiException e) {
            logger.error(new ParameterizedMessage("Error during sending of a message to chatId {}", chatId), e);
        }
    }

    public synchronized void sendSpecifiedText(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        addButtonsToTheMessage(sendMessage, new String[]{"Контакты ТС","Новая заяка"});
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
            if(contactInfo.equalsIgnoreCase(text)){
                logger.info(new ParameterizedMessage("Contact info has been sent to chatId: {}", chatId));
            } else {
                logger.info(new ParameterizedMessage("Text: \"{}\" was sent to chatId: {}", text, chatId));
            }
        } catch (TelegramApiException e) {
           logger.error(new ParameterizedMessage("Error during sending of a message to chatId {}", chatId), e);
        }
    }

    public synchronized void addButtonsToTheMessage(SendMessage sendMessage, String[] buttons){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow();

        for(String button : buttons){
            firstRow.add(new KeyboardButton(button));
        }
        keyboard.add(firstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }

    public String getBotUsername() {
        return botUsername;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setFormSubmitter(GoogleFormSubmitter formSubmitter) {
        this.formSubmitter = formSubmitter;
    }

    public void setGreetingText(String greetingText) {
        this.greetingText = greetingText;
    }
}