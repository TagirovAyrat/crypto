package ru.airiva.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import ru.airiva.UpdateHandler;
import ru.airiva.handler.UpdateHandlerFactory;
import ru.airiva.properties.BotProperties;

public class KryptoPrideWebHookBot extends TelegramWebhookBot {

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
        UpdateHandler handler = updateHandlerFactory.getHandler(update);
        return handler != null ? handler.handle(update) : null;
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
