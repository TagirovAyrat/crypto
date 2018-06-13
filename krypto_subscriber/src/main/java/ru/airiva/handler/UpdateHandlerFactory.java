package ru.airiva.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Update;
@Component
public class UpdateHandlerFactory {

    private MessageHandler messageHandler;
    private CallbackQueryHandler callbackQueryHandler;

    @Autowired
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Autowired
    public void setCallbackQueryHandler(CallbackQueryHandler callbackQueryHandler) {
        this.callbackQueryHandler = callbackQueryHandler;
    }

    public UpdateHandler getHandler(Update update) {
        if (update == null) return null;
        UpdateHandler updateHandler = null;
        if (update.hasMessage()) {
            updateHandler = messageHandler;
        } else if (update.hasCallbackQuery()) {
            updateHandler = callbackQueryHandler;
        }
        return updateHandler;
    }

}
