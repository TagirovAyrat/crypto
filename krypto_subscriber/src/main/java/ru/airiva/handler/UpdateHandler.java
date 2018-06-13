package ru.airiva.handler;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;

public interface UpdateHandler {
    BotApiMethod handle(Update update);
}
