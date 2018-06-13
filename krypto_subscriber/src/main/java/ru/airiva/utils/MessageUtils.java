package ru.airiva.utils;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import ru.airiva.entity.SessionData;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageUtils {

    public static SendMessage replyWithReplyKeyboard(String string, Integer chatId, ReplyKeyboard keyboard)   {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(string);
        sendMessage.setReplyMarkup(keyboard);
        return sendMessage;
    }

    public static SendMessage sendErrorMessageWithLocale(Integer id, String unknownOperation) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(id));
        sendMessage.setText(unknownOperation);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        return sendMessage;
    }

    public static SendMessage sendDefaultErrorMessage(Integer id) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(id));
        sendMessage.setText("Unknown command, plese try /help to get command list");
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        return sendMessage;
    }

    public static Integer getTlgIdDependsOnUpdateType(Update update) {
        Integer tgId = null;
        if (update == null) return null;
        if (update.hasMessage()) {
            tgId = update.getMessage().getFrom().getId();
        } else if (update.hasCallbackQuery()) {
            tgId = update.getCallbackQuery().getFrom().getId();
        }
        return tgId;
    }

    public SendMessage replyAndRemoveKeyboard(String string, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(string);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        return sendMessage;
    }


    public static SendMessage unknownOperationResponse(SessionData sessionData, Integer id) {
        if (sessionData != null) {
            Locale locale = sessionData.getLocale();
            ResourceBundle resources = ResourceBundle.getBundle("Resources", locale);
            String unknownOperation = resources.getString("unknownOperation");
            return sendErrorMessageWithLocale(id, unknownOperation);
        } else {
            return sendDefaultErrorMessage(id);
        }
    }
    public static String extractCommandFromMessage(String message) {
        Pattern pattern = Pattern.compile("\\/([A-Za-z_]+)[\\s]?");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }



}
