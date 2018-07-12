package ru.airiva.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.airiva.handler.UpdateHandlerFactory;
import ru.airiva.uam.TlgBotCommandsText;

import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class KryptoPrideWebHookBot extends TelegramWebhookBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(KryptoPrideWebHookBot.class);
    private UpdateHandlerFactory updateHandlerFactory;
    private String userName;
    private String token;
    private Map<String, List<TlgBotCommandsText>> tlgBotCommandsText;
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Map<String, List<TlgBotCommandsText>> getTlgBotCommandsText() {
        return tlgBotCommandsText;
    }

    public void setTlgBotCommandsText(Map<String, List<TlgBotCommandsText>> tlgBotCommandsText) {
        this.tlgBotCommandsText = tlgBotCommandsText;
    }

    @Autowired
    public void setUpdateHandlerFactory(UpdateHandlerFactory updateHandlerFactory) {
        this.updateHandlerFactory = updateHandlerFactory;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        KryptoPrideWebHookBot kryptoPrideWebHookBot = this;
        BotApiMethod method = updateHandlerFactory.getHandler(update).handle(kryptoPrideWebHookBot, update);
        try {
             execute(method);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return  null;
    }
    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public String getBotPath() {
        return null;
    }

}
