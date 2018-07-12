package ru.airiva.utils;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.airiva.entities.TlgBotTranslationEntity;
import ru.airiva.entity.SessionData;
import ru.airiva.uam.TlgBotCommandsText;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class KeyboardUtils {


    public static Map<String, List<TlgBotCommandsText>> getCommandsTextFromTlgBotEntity(Set<TlgBotTranslationEntity> tlgBotTranslationEntities) {
        Map<String, List<TlgBotCommandsText>> commands = new HashMap<>();
        List<TlgBotCommandsText> ruBotCommandsTexts = new ArrayList<>();
        List<TlgBotCommandsText> enBotCommandsTexts = new ArrayList<>();
        List<TlgBotTranslationEntity> ru = tlgBotTranslationEntities.stream().filter(tlgBotTranslationEntity -> tlgBotTranslationEntity.getLocale().equalsIgnoreCase("ru")).collect(Collectors.toList());
        commandBuilder(ru, ruBotCommandsTexts);
        List<TlgBotTranslationEntity> en = tlgBotTranslationEntities.stream().filter(tlgBotTranslationEntity -> tlgBotTranslationEntity.getLocale().equalsIgnoreCase("ru")).collect(Collectors.toList());
        commandBuilder(en, enBotCommandsTexts);
        commands.put("ru", ruBotCommandsTexts);
        commands.put("en", enBotCommandsTexts);
        return commands;
}
    private static final int MAX_ROW = 3;

    public static void commandBuilder(List<TlgBotTranslationEntity> ru, List<TlgBotCommandsText> botCommandsTexts) {
        ru.forEach(tlgBotTranslationEntity -> {
            botCommandsTexts.add(new TlgBotCommandsText() {{
                setCommand(tlgBotTranslationEntity.getCommand());
                setCommandText(tlgBotTranslationEntity.getCommandText());
                setStartEmoji(tlgBotTranslationEntity.getEmojiAtBegin());
                setEndEmoji(tlgBotTranslationEntity.getEmojiAtEnd());
            }});
        });
    }

    public static List<List<String>> getLocaleKeyboard() {
        List<List<String>> arrayLists = new ArrayList<>();

        arrayLists.add(new ArrayList<String>() {{
            add(EmojiParser.parseToUnicode(Emoji.RU) + "RU");
            add(EmojiParser.parseToUnicode(Emoji.UK) + "UK");
        }});

        return arrayLists;
    }

    public static ReplyKeyboardMarkup generateReplyKeyboard(List<List<String>> inputRow) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        for (List<String> anInputRow : inputRow) {
            KeyboardRow row = new KeyboardRow();
            for (String anAnInputRow : anInputRow) {
                row.add(anAnInputRow);
            }
            keyboard.add(row);
        }
        keyboardMarkup.setKeyboard(keyboard);

        return keyboardMarkup;
    }

    public static List<List<String>> getStaticKeyboard(SessionData sessionData) {
        List<List<String>> tmpKeyboard = new ArrayList<>();
        List<List<String>> resutlKeyboard = new ArrayList<>();
        sessionData.getTlgBotCommandsTexts().stream().filter(tlgBotCommandsText -> tlgBotCommandsText.getLocale().equalsIgnoreCase(sessionData.getLocale())).forEach(tlgBotCommandsText -> {
            {
                tmpKeyboard.add(getKeyboard(tlgBotCommandsText));
            }
        });
        final int[] k = {0};
        for (int i = 0; i < tmpKeyboard.size(); i++) {
            List<String> strings = new ArrayList<>();
            for (int j = 0; j <= (i > MAX_ROW - 1 ? MAX_ROW - 1 : i); j++) {
                strings.add(String.valueOf(tmpKeyboard.get(k[0]++)));
            }
            i = k[0] - 1;
            resutlKeyboard.add(strings);
        }
        return tmpKeyboard;

    }

    private static String getEmoji(String emoji) {
        return "".equalsIgnoreCase(emoji) ? EmojiParser.parseToUnicode(emoji) : "";
    }

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }

    public static List<String> getKeyboard(TlgBotCommandsText tlgBotCommandsText) {
        List<String> row = new ArrayList<>();
        row.add(getEmoji(tlgBotCommandsText.getStartEmoji()) + " " +
                tlgBotCommandsText.getCommand() + " " + getEmoji(tlgBotCommandsText.getEndEmoji()));
        return null;
    }
}


