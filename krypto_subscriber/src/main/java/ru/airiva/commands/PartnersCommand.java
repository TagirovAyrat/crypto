package ru.airiva.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.airiva.annotations.BotContextMethod;
import ru.airiva.entity.SessionData;

public class PartnersCommand implements CommandMarker {
    @Override
    @BotContextMethod(step = "initial")
    public SendMessage initial(Update update)  {

        return null;
    }
}
