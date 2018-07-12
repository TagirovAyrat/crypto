package ru.airiva.mappers;

import org.springframework.stereotype.Component;
import ru.airiva.bot.KryptoPrideWebHookBot;
import ru.airiva.entities.PersonEntity;
import ru.airiva.entities.TlgBotEntity;
import ru.airiva.entities.TlgBotTranslationEntity;
import ru.airiva.uam.TlgBotCommandsText;
import ru.airiva.utils.KeyboardUtils;
import ru.airiva.utils.SpringContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TlgBotMapper {
    public ConcurrentHashMap<String, KryptoPrideWebHookBot> mapperFromTlgBotToWebHookBot(List<TlgBotEntity> tlgBotEntity) {
        ConcurrentHashMap<String, KryptoPrideWebHookBot> botList = new ConcurrentHashMap<>();
        for (TlgBotEntity botEntity : tlgBotEntity) {
            KryptoPrideWebHookBot kryptoPrideWebHookBot = SpringContext.getContext().getBean(KryptoPrideWebHookBot.class);
            kryptoPrideWebHookBot.setUserName(botEntity.getUsername());
            kryptoPrideWebHookBot.setToken(botEntity.getToken());
            kryptoPrideWebHookBot.setTlgBotCommandsText(KeyboardUtils.getCommandsTextFromTlgBotEntity(botEntity.getTlgBotTranslationEntities()));
            botList.put(botEntity.getToken(), kryptoPrideWebHookBot);
        }
        return botList;
    }

    public TlgBotEntity mapperFromWebHookBotToTlgBotEntity(String botToken, PersonEntity personById, String botUsername) {
        return null;
    }
}
