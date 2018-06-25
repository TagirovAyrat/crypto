package ru.airiva.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

@Component
@BotContext(command = CommandList.START)
public class StartCommand implements CommandMarker {

    private AreaRedisRepo areaRedisRepo;
    public static final Logger LOGGER = LoggerFactory.getLogger(StartCommand.class);

    @Autowired
    public void setAreaRedisRepo(AreaRedisRepo areaRedisRepo) {
        this.areaRedisRepo = areaRedisRepo;
    }

    @Override
    @BotContextMethod(step = "initial")
    public SendMessage initial(Update update) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        SessionData sessionData = areaRedisRepo.get(String.valueOf(tgId));
        if (sessionData == null || sessionData.getLocale() == null) {
            sessionData = new SessionData();
            ReplyKeyboardMarkup replyKeyboardMarkup = KeyboardUtils.generateReplyKeyboard(KeyboardUtils.getLocaleKeyboard());
            sessionData.setChatId(String.valueOf(tgId));
            sessionData.setCurrentStep("locale");
            sessionData.setCurrentCommand("start");
            areaRedisRepo.put(String.valueOf(tgId), sessionData);
            return MessageUtils.replyWithReplyKeyboard("Choose your Language", tgId, replyKeyboardMarkup);
        } else {
            Locale locale = sessionData.getLocale();
            ResourceBundle resources = ResourceBundle.getBundle("lang", locale);
            ResourceBundle utf8PropertyResourceBundle = MessageUtils.createUtf8PropertyResourceBundle(resources);
            String greetings = utf8PropertyResourceBundle.getString("greetings");
            ReplyKeyboardMarkup replyKeyboardMarkup = null;
            try {
                replyKeyboardMarkup = KeyboardUtils.generateReplyKeyboard(KeyboardUtils.getKeyboardDependsOnCommands());
            } catch (IllegalAccessException | IOException e) {
                LOGGER.error("Execution method error", e.getMessage());
            }
            return MessageUtils.replyWithReplyKeyboard(greetings, tgId, replyKeyboardMarkup);
        }
    }

    @BotContextMethod(step = "locale", ifInputTextEquals = "RU")
    public SendMessage localeRu(Update update) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        SessionData sessionData = areaRedisRepo.get(String.valueOf(tgId));
        sessionData.setLocale(Locale.forLanguageTag("ru"));
        areaRedisRepo.put(String.valueOf(tgId), sessionData);
        return initial(update);
    }

    @BotContextMethod(step = "locale", ifInputTextEquals = "EN")
    public SendMessage localeEn(Update update) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        SessionData sessionData = areaRedisRepo.get(String.valueOf(tgId));
        sessionData.setLocale(Locale.ENGLISH);
        areaRedisRepo.put(String.valueOf(tgId), sessionData);
        return initial(update);
    }

}
