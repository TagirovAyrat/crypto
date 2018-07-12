package ru.airiva.handler;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import ru.airiva.bot.KryptoPrideWebHookBot;

public interface UpdateHandler {
    BotApiMethod handle(KryptoPrideWebHookBot kryptoPrideWebHookBot, Update update);
}
