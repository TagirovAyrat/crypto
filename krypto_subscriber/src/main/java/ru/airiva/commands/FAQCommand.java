package ru.airiva.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.airiva.annotations.BotContextMethod;
import ru.airiva.entity.SessionData;
import ru.airiva.utils.KeyboardUtils;
import ru.airiva.utils.MessageUtils;

public class FAQCommand implements CommandMarker {
    @Override
    @BotContextMethod(step = "initial")
    public SendMessage initial(Update update) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        //TODO Забрать текст из базы данных
     return   MessageUtils.replyWithReplyKeyboard("STUB", tgId, KeyboardUtils.generateReplyKeyboard(KeyboardUtils.getKeyboardDependsOnCommands()));
    }
}
