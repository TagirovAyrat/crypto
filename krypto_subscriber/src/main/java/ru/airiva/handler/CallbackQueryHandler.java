package ru.airiva.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import ru.airiva.UpdateHandler;
@Component
public class CallbackQueryHandler implements UpdateHandler {

    @Override
    public BotApiMethod handle(Update update) {
        return null;
    }
}
