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

    public TlgBotEntity mapperFromWebHookBotToTlgBotEntity(KryptoPrideWebHookBot kryptoPrideWebHookBot, PersonEntity personById) {
        TlgBotEntity tlgBotEntity = new TlgBotEntity();
        tlgBotEntity.setToken(kryptoPrideWebHookBot.getBotToken());
        tlgBotEntity.setPersonEntity(personById);
        tlgBotEntity.setTlgBotTranslationEntities(mapperFromTlgCommandsToTransaltionEntity(kryptoPrideWebHookBot.getTlgBotCommandsText()));
        tlgBotEntity.setUsername(kryptoPrideWebHookBot.getBotUsername());
        return tlgBotEntity;
    }

    private Set<TlgBotTranslationEntity> mapperFromTlgCommandsToTransaltionEntity(Map<String, List<TlgBotCommandsText>> tlgBotCommandsText) {
        Set<TlgBotTranslationEntity> tlgBotTranslationEntities = new HashSet<>();
        tlgBotCommandsText.values().forEach(botCommandsTexts -> {
            botCommandsTexts.forEach(tlgBotCommandsText1 -> {
                tlgBotTranslationEntities.add(new TlgBotTranslationEntity() {{
                    setCommand(tlgBotCommandsText1.getCommand());
                    setLocale(tlgBotCommandsText1.getLocale());
                    setCommandText(tlgBotCommandsText1.getCommandText());
                    setEmojiAtBegin(tlgBotCommandsText1.getStartEmoji());
                    setEmojiAtEnd(tlgBotCommandsText1.getEndEmoji());
                }});
            });
        });
        return tlgBotTranslationEntities;
    }
}
