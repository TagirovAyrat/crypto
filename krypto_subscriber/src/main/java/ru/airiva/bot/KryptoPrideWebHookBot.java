package ru.airiva.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import ru.airiva.handler.UpdateHandler;
import ru.airiva.handler.UpdateHandlerFactory;
import ru.airiva.properties.BotProperties;

@Component
public class KryptoPrideWebHookBot extends TelegramWebhookBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(KryptoPrideWebHookBot.class);
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

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        BotApiMethod method = updateHandlerFactory.getHandler(update).handle(update);
        try {
             execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return  null;
    }


    @Override
    public String getBotUsername() {
        return botProperties.username;
    }

    @Override
    public String getBotToken() {
        return botProperties.token;
    }

    @Override
    public String getBotPath() {
        return botProperties.path;
    }
}
