package ru.airiva.utils;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
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

    //TODO Загрузит из базы данных
    public static List<List<String>> getKeyboardDependsOnCommands() throws IllegalAccessException, IOException {
        String[] tmp = new String[]{
                "FAQ=:o: Faq",
                "ABOUT=:o: About",
                "PARTNERS=:o: Partners",
                "PACKAGES=:o: Packages",
                "PROFILE=:o: Profile",
                "UNPAIDORDERS=:o: UnpaidOrdes",
                "EXCHANGE=:o: Echange",
                "SUPPORT=:o: Support",
                "CANCEL=:o: Cancel",
        };
        List<String> commandsForMainMenuKeyboards = new ArrayList<>();
//        ClassLoader classLoader = KeyboardUtils.class.getClassLoader();
//        File file = new File(classLoader.getResource("keyboard.properties").getFile());
//        Properties appProps = new Properties();
//        appProps.load(new FileInputStream(file));
//        for (Object o : appProps.keySet()) {
//            String s = appProps.get(o).toString();
//            String emoji = s.substring(s.indexOf(s.charAt(0)), s.indexOf(" "));
//            String command = s.substring(s.indexOf(" ") + 1);
//            commandsForMainMenuKeyboards.add(EmojiParser.parseToUnicode(emoji) + command);
//        }
        for (String s : tmp) {
            String emoji = s.substring(s.indexOf("=") + 1, s.indexOf(" "));
            String command = s.substring(s.indexOf(" ") + 1);
            commandsForMainMenuKeyboards.add(EmojiParser.parseToUnicode(emoji) + command);
        }
//        BotProperties botProperties = SpringContextProvider.getApplicationContext().getBean(BotProperties.class);
//        Integer rowsCount = botProperties.rowsCount;
        Integer rowsCount = 3;
        List<List<String>> keyboard = new ArrayList<>();
        if (rowsCount > commandsForMainMenuKeyboards.size())
            throw new IllegalAccessException("Wrong rows Count");
        int mod = commandsForMainMenuKeyboards.size() / rowsCount;
        int div = commandsForMainMenuKeyboards.size() % rowsCount;
        int buttonCounter = 0;
        for (int i = 0; i < rowsCount; i++) {
            System.out.println(i);
            List<String> rows = new ArrayList<>();
            for (int j = 0; j < mod; j++) {
                rows.add(commandsForMainMenuKeyboards.get(buttonCounter));
                buttonCounter++;
            }
            keyboard.add(rows);
            if (i + 1 == rowsCount && div != 0) {
                for (int j = 0; j < div; j++) {
                    rows.add(commandsForMainMenuKeyboards.get(buttonCounter));
                }
            }
        }
        return keyboard;
    }

    public static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.stream(e.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}