package ru.airiva.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.airiva.annotations.BotContext;
import ru.airiva.annotations.BotContextMethod;
import ru.airiva.entity.SessionData;
import ru.airiva.enums.CommandList;
import ru.airiva.repo.AreaRedisRepo;
import ru.airiva.utils.KeyboardUtils;
import ru.airiva.utils.MessageUtils;

import java.util.Locale;
import java.util.ResourceBundle;

@BotContext(command = CommandList.START)
public class StartCommand implements CommandMarker{

    private AreaRedisRepo areaRedisRepo;

    @Autowired
    public void setAreaRedisRepo(AreaRedisRepo areaRedisRepo) {
        this.areaRedisRepo = areaRedisRepo;
    }

    @Override
    @BotContextMethod(step = "initial")
    public SendMessage initial(Update update) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        SessionData sessionData = areaRedisRepo.get(String.valueOf(tgId));
        if (sessionData.getLocale() == null) {
            ReplyKeyboardMarkup replyKeyboardMarkup = KeyboardUtils.generateReplyKeyboard(KeyboardUtils.getLocaleKeyboard());
            sessionData.setChatId(String.valueOf(tgId));
            sessionData.setCurrentStep("locale");
            return MessageUtils.replyWithReplyKeyboard("Choose your Language", tgId, replyKeyboardMarkup);
        } else {
            Locale locale = sessionData.getLocale();
            ResourceBundle resources = ResourceBundle.getBundle("Resources", locale);
            String greetings = resources.getString("greetings");
            ReplyKeyboardMarkup replyKeyboardMarkup = KeyboardUtils.generateReplyKeyboard(KeyboardUtils.getKeyboardDependsOnCommands());
            return MessageUtils.replyWithReplyKeyboard(greetings, tgId, replyKeyboardMarkup);
        }
    }
    @BotContextMethod(step = "locale", ifInputTextEquals = "RU")
    public void localeRu(Update update) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        SessionData sessionData = areaRedisRepo.get(String.valueOf(tgId));
        sessionData.setLocale(Locale.forLanguageTag("ru"));
        initial(update);
    }
    @BotContextMethod(step = "locale", ifInputTextEquals = "EN")
    public void localeEn(Update update) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        SessionData sessionData = areaRedisRepo.get(String.valueOf(tgId));
        sessionData.setLocale(Locale.ENGLISH);
        initial(update);
    }
}
