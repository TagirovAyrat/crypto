package ru.airiva.handler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import ru.airiva.bot.KryptoPrideWebHookBot;

@Component
public class CallbackQueryHandler implements UpdateHandler {

    @Override
    public BotApiMethod handle(KryptoPrideWebHookBot kryptoPrideWebHookBot, Update update) {

        return null;
    }
}
