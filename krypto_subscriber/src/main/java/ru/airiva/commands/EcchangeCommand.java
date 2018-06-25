package ru.airiva.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.airiva.entity.SessionData;
@Component
public class EcchangeCommand implements CommandMarker {
    @Override
    public SendMessage initial(Update update)  {

        return null;
    }
}
