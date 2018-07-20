package ru.airiva.utils;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import ru.airiva.bot.KryptoPrideWebHookBot;
import ru.airiva.entity.SessionData;
import ru.airiva.uam.TlgBotCommandsText;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
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
        sendMessage.setText("Unknown command, please try /help to get command list");
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
        if (sessionData != null &&
                sessionData.getLocale() != null &&
                sessionData.getTlgBotCommandsTexts() != null) {
            String unknownOperation = sessionData.getTlgBotCommandsTexts().stream().filter(tlgBotCommandsText -> tlgBotCommandsText.getCommand().equalsIgnoreCase("unknownoperation")).findFirst().get().getTranslatedCommand();
            return sendErrorMessageWithLocale(id, unknownOperation);
        } else {
            return sendDefaultErrorMessage(id);
        }
    }

    public static String extractCommandFromMessage(KryptoPrideWebHookBot kryptoPrideWebHookBot, String message) {
        String messageWithoutSlash = null;
        String resultMessage = null;
        Pattern pattern = Pattern.compile("(/)([A-Za-z]+)[\\s]?");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            messageWithoutSlash =  matcher.group(2);
        }
        for (List<TlgBotCommandsText> value : kryptoPrideWebHookBot.getTlgBotCommandsText().values()) {
            for (TlgBotCommandsText tlgBotCommandsText : value) {
                if (message.contains(tlgBotCommandsText.getCommand())) {
                    resultMessage = tlgBotCommandsText.getCommand();
                    break;
                }
            }
        }
        return messageWithoutSlash != null ? messageWithoutSlash : resultMessage;
    }

    //Крутая штука оставить на всякий случай
    public static ResourceBundle createUtf8PropertyResourceBundle(
            final ResourceBundle bundle) {
        if (!(bundle instanceof PropertyResourceBundle)) {
            return bundle;
        }
        return new Utf8PropertyResourceBundle((PropertyResourceBundle) bundle);
    }

    /**
     * Resource Bundle that does the hard work
     */
    private static class Utf8PropertyResourceBundle extends ResourceBundle {

        /**
         * Bundle with unicode data
         */
        private final PropertyResourceBundle bundle;

        /**
         * Initializing constructor
         *
         * @param bundle
         */
        private Utf8PropertyResourceBundle(final PropertyResourceBundle bundle) {
            this.bundle = bundle;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Enumeration getKeys() {
            return bundle.getKeys();
        }

        @Override
        protected Object handleGetObject(final String key) {
            final String value = bundle.getString(key);
            if (value == null)
                return null;
            try {
                return new String(value.getBytes("ISO-8859-1"), "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException("Encoding not supported", e);
            }
        }
    }

}
