package ru.airiva.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.airiva.handler.UpdateHandlerFactory;
import ru.airiva.properties.BotProperties;


@Component
public class KryptoPrideLongPolingBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(KryptoPrideLongPolingBot.class);

    private BotProperties botProperties;
    private UpdateHandlerFactory updateHandlerFactory;
    @Autowired
    public void setBotProperties(BotProperties botProperties) {
        this.botProperties = botProperties;
    }
    @Autowired
    public void setUpdateHandlerFactory(UpdateHandlerFactory updateHandlerFactory) {
        this.updateHandlerFactory = updateHandlerFactory;
    }

    public KryptoPrideLongPolingBot(DefaultBotOptions options) {
        super(options);
    }

    public KryptoPrideLongPolingBot() {
    }

    @Override
    public void onUpdateReceived(Update update) {
        BotApiMethod method = updateHandlerFactory.getHandler(update).handle(update);
        try {
            execute(method);
        } catch (TelegramApiException e) {
            LOGGER.error("Execution method error", e);
        }

    }

    @Override
    public String getBotUsername() {
        LOGGER.info("getBotUserName {}", botProperties.username);
        return botProperties.username;
    }

    @Override
    public String getBotToken() {
        LOGGER.info("getBotToken: {}", botProperties.token);
        return botProperties.token;
    }
}
