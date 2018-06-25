package ru.airiva.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.airiva.annotations.BotContextMethod;
import ru.airiva.entity.SessionData;
import ru.airiva.utils.KeyboardUtils;
import ru.airiva.utils.MessageUtils;
@Component
public class FAQCommand implements CommandMarker {

    @Override
    public SendMessage initial(Update update) {
        return new SendMessage(){{
            setText("FAQ");
        }};
    }
}
