package ru.airiva.utils;

import com.vdurmont.emoji.EmojiParser;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.airiva.enums.CommandList;

import java.util.ArrayList;
import java.util.List;

public class KeyboardUtils {
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

    public static List<List<String>> getKeyboardDependsOnCommands() {
        List<List<String>> arrayLists = new ArrayList<>();
        for (CommandList value : CommandList.values()) {
            arrayLists.add(new ArrayList<String>() {{
                add(EmojiParser.parseToUnicode(Emoji.SCROLL) + value.name());
            }});
        }
        return arrayLists;
    }
}
