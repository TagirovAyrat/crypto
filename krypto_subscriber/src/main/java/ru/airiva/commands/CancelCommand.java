package ru.airiva.commands;

import org.hibernate.annotations.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.airiva.annotations.BotContext;
import ru.airiva.annotations.BotContextMethod;
import ru.airiva.entity.SessionData;
import ru.airiva.enums.CommandList;
import ru.airiva.repo.AreaRedisRepo;
import ru.airiva.utils.MessageUtils;

import java.util.Locale;
import java.util.ResourceBundle;

@Component
@BotContext(command = CommandList.CANCEL)
public class CancelCommand implements CommandMarker {

    private MessageUtils messageUtils;
    private AreaRedisRepo areaRedisRepo;
    private StartCommand startCommand;
    @Autowired
    public void setMessageUtils(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }

    @Autowired
    public void setAreaRedisRepo(AreaRedisRepo areaRedisRepo) {
        this.areaRedisRepo = areaRedisRepo;
    }
    @Autowired
    public void setStartCommand(StartCommand startCommand) {
        this.startCommand = startCommand;
    }

    @BotContextMethod
    public SendMessage initial(Update update) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        SessionData sessionData = areaRedisRepo.get(String.valueOf(tgId));
        Locale locale = sessionData.getLocale() != null ? sessionData.getLocale() : DEFAULT_LOCALE;
        ResourceBundle resources = ResourceBundle.getBundle("lang", locale);
        ResourceBundle utf8PropertyResourceBundle = MessageUtils.createUtf8PropertyResourceBundle(resources);
        String contextCleared = utf8PropertyResourceBundle.getString("contextCleared");
        areaRedisRepo.remove(String.valueOf(tgId));
        return startCommand.initial(update);
//        return messageUtils.replyAndRemoveKeyboard(contextCleared, String.valueOf(tgId));
    }
}
