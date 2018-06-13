package ru.airiva.commands;

import org.springframework.beans.factory.annotation.Autowired;
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

@BotContext(command = CommandList.CANCEL)
public class CancelCommand implements CommandMarker {

    private MessageUtils messageUtils;
    private AreaRedisRepo areaRedisRepo;
    @Autowired
    public void setMessageUtils(MessageUtils messageUtils) {
        this.messageUtils = messageUtils;
    }
    @Autowired
    public void setAreaRedisRepo(AreaRedisRepo areaRedisRepo) {
        this.areaRedisRepo = areaRedisRepo;
    }

    @BotContextMethod
    public SendMessage initial(Update update) {
        Integer tgId = MessageUtils.getTlgIdDependsOnUpdateType(update);
        SessionData sessionData = areaRedisRepo.get(String.valueOf(tgId));
        Locale locale = sessionData.getLocale() != null ? sessionData.getLocale() : DEFAULT_LOCALE;
        ResourceBundle resources = ResourceBundle.getBundle("Resources", locale);
        String contextCleared = resources.getString("contextCleared");
        return messageUtils.replyAndRemoveKeyboard(contextCleared, String.valueOf(tgId));
    }
}
