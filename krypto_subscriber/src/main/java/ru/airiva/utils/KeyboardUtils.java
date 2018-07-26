package ru.airiva.utils;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.airiva.entity.SessionData;
import ru.airiva.service.KryptoBotModuleService;
import ru.airiva.uam.TlgBotCommandsText;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class KeyboardUtils {

    private KryptoBotModuleService kryptoBotModuleService;
    private static final int MAX_ROW = 3;

    @Autowired
    public void setKryptoBotModuleService(KryptoBotModuleService kryptoBotModuleService) {
        this.kryptoBotModuleService = kryptoBotModuleService;
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

    public List<List<String>> getStaticKeyboard(SessionData sessionData) {
        List<List<String>> tmpKeyboard = new ArrayList<>();
        List<List<String>> resutlKeyboard = new ArrayList<>();

        sessionData.getTlgBotCommandsTexts().stream().filter(tlgBotCommandsText -> tlgBotCommandsText.getLocale().equalsIgnoreCase(sessionData.getLocale()) &&
                isCommand(tlgBotCommandsText.getCommand())).forEach(tlgBotCommandsText -> {
            {
                tmpKeyboard.add(getKeyboard(tlgBotCommandsText));
            }
        });
        final int[] k = {0};
        for (int i = 0; i < tmpKeyboard.size(); i++) {
            List<String> strings = new ArrayList<>();
            for (int j = 0; (j <= (i > MAX_ROW - 1 ? MAX_ROW - 1 : i)) && (tmpKeyboard.size() - i >= MAX_ROW) ; j++) {
                strings.add(String.valueOf(tmpKeyboard.get(k[0]++)));
            }
            if (tmpKeyboard.size() - i < MAX_ROW) {
                for (int j = i; j < tmpKeyboard.size(); j++) {
                    strings.add(String.valueOf(tmpKeyboard.get(k[0]++)));
                }
                break;
            }
            i = k[0] - 1;
            resutlKeyboard.add(strings);
        }
        return resutlKeyboard;
    }

    private boolean isCommand(String command) {
        return kryptoBotModuleService.getComandsFroKeyboard().stream().anyMatch(s -> command.contains(s));
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
        return row;
    }

    public static Map<String, List<TlgBotCommandsText>> parseCommandsFromJson(List<TlgBotCommandsText> commands) {
        Map<String, List<TlgBotCommandsText>> commandMap = new HashMap<>();
        commandMap.put("ru", commands.stream().filter(tlgBotCommandsText -> tlgBotCommandsText.getLocale().equalsIgnoreCase("ru")).collect(Collectors.toList()));
        commandMap.put("en", commands.stream().filter(tlgBotCommandsText -> tlgBotCommandsText.getLocale().equalsIgnoreCase("en")).collect(Collectors.toList()));
        return commandMap;
    }
}


