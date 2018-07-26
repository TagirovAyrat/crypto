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
import ru.airiva.bot.KryptoPrideWebHookBot;
import ru.airiva.entity.SessionData;
import ru.airiva.enums.CommandList;
import ru.airiva.repo.AreaRedisRepo;
import ru.airiva.utils.KeyboardUtils;
import ru.airiva.utils.MessageUtils;

@Component
@BotContext(command = CommandList.START)
public class StartCommand implements CommandMarker {

    private AreaRedisRepo areaRedisRepo;
    private KeyboardUtils keyboardUtils;
    public static final Logger LOGGER = LoggerFactory.getLogger(StartCommand.class);

    @Autowired
    public void setAreaRedisRepo(AreaRedisRepo areaRedisRepo) {
        this.areaRedisRepo = areaRedisRepo;
    }
    @Autowired
    public void setKeyboardUtils(KeyboardUtils keyboardUtils) {
        this.keyboardUtils = keyboardUtils;
    }

    @Override
    @BotContextMethod(step = "initial")
    public SendMessage initial(Update update) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        LOGGER.debug("Start command for user " + tgId);
        SessionData sessionData = areaRedisRepo.get(String.valueOf(tgId));
        if (sessionData == null) {
            sessionData = new SessionData();
            ReplyKeyboardMarkup replyKeyboardMarkup = KeyboardUtils.generateReplyKeyboard(KeyboardUtils.getLocaleKeyboard());
            sessionData.setTlgId(String.valueOf(tgId));
            sessionData.setCurrentStep("locale");
            sessionData.setCurrentCommand("start");
            areaRedisRepo.put(String.valueOf(tgId), sessionData);
            return MessageUtils.replyWithReplyKeyboard("Choose your Language", tgId, replyKeyboardMarkup);
        } else {
            String greetings = sessionData.getTlgBotCommandsTexts().stream().filter(tlgBotCommandsText ->
                    tlgBotCommandsText.getCommand().equalsIgnoreCase("greetings")).findFirst().get().getTranslatedCommand();
            ReplyKeyboardMarkup replyKeyboardMarkup;
            replyKeyboardMarkup = KeyboardUtils.generateReplyKeyboard(keyboardUtils.getStaticKeyboard(sessionData));
            return MessageUtils.replyWithReplyKeyboard(greetings, tgId, replyKeyboardMarkup);
        }
    }

    @BotContextMethod(step = "locale", ifInputTextEquals = "RU")
    public SendMessage localeRu(Update update, KryptoPrideWebHookBot kryptoPrideWebHookBot) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        SessionData sessionData = areaRedisRepo.get(String.valueOf(tgId));
        sessionData.setLocale("ru");
        sessionData.setTlgBotCommandsTexts(kryptoPrideWebHookBot.getTlgBotCommandsText().get("ru"));
        areaRedisRepo.put(String.valueOf(tgId), sessionData);
        return initial(update);
    }

    @BotContextMethod(step = "locale", ifInputTextEquals = "EN")
    public SendMessage localeEn(Update update, KryptoPrideWebHookBot kryptoPrideWebHookBot) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        SessionData sessionData = areaRedisRepo.get(String.valueOf(tgId));
        sessionData.setLocale("en");
        sessionData.setTlgBotCommandsTexts(kryptoPrideWebHookBot.getTlgBotCommandsText().get("en"));
        areaRedisRepo.put(String.valueOf(tgId), sessionData);
        return initial(update);
    }
}
