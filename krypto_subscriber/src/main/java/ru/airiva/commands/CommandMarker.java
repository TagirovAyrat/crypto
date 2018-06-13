package ru.airiva.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;

import java.util.Locale;

public interface CommandMarker {

    SendMessage initial(Update update);
    Locale DEFAULT_LOCALE = Locale.ENGLISH;
}
