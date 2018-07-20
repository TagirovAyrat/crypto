package ru.airiva.mappers;

import org.springframework.stereotype.Component;
import ru.airiva.bot.KryptoPrideWebHookBot;
import ru.airiva.entities.PersonEntity;
import ru.airiva.entities.TlgBotEntity;
import ru.airiva.entities.TlgBotTranslationEntity;
import ru.airiva.uam.TlgBotCommandsText;
import ru.airiva.utils.SpringContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class TlgBotMapper {
    public ConcurrentHashMap<String, KryptoPrideWebHookBot> mapperFromTlgBotEntityToWebHookBot(List<TlgBotEntity> tlgBotEntity) {
        ConcurrentHashMap<String, KryptoPrideWebHookBot> botList = new ConcurrentHashMap<>();
        for (TlgBotEntity botEntity : tlgBotEntity) {
            KryptoPrideWebHookBot kryptoPrideWebHookBot = SpringContext.getContext().getBean(KryptoPrideWebHookBot.class);
            kryptoPrideWebHookBot.setUserName(botEntity.getUsername());
            kryptoPrideWebHookBot.setToken(botEntity.getToken());
            kryptoPrideWebHookBot.setTlgBotCommandsText(getCommandsTextFromTlgBotEntity(botEntity.getTlgBotTranslationEntities()));
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
                    setTranslatedCommand(tlgBotCommandsText1.getTranslatedCommand());
                    setCommandText(tlgBotCommandsText1.getTranslatedCommandText());
                    setEmojiAtBegin(tlgBotCommandsText1.getStartEmoji());
                    setEmojiAtEnd(tlgBotCommandsText1.getEndEmoji());
                }});
            });
        });
        return tlgBotTranslationEntities;
    }

    public static Map<String, List<TlgBotCommandsText>> getCommandsTextFromTlgBotEntity(Set<TlgBotTranslationEntity> tlgBotTranslationEntities) {
        Map<String, List<TlgBotCommandsText>> commands = new HashMap<>();
        List<TlgBotCommandsText> ruBotCommandsTexts = new ArrayList<>();
        List<TlgBotCommandsText> enBotCommandsTexts = new ArrayList<>();
        List<TlgBotTranslationEntity> ru = tlgBotTranslationEntities.stream().filter(tlgBotTranslationEntity -> tlgBotTranslationEntity.getLocale().equalsIgnoreCase("ru")).collect(Collectors.toList());
        commandBuilder(ru, ruBotCommandsTexts);
        List<TlgBotTranslationEntity> en = tlgBotTranslationEntities.stream().filter(tlgBotTranslationEntity -> tlgBotTranslationEntity.getLocale().equalsIgnoreCase("ru")).collect(Collectors.toList());
        commandBuilder(en, enBotCommandsTexts);
        commands.put("ru", ruBotCommandsTexts);
        commands.put("en", enBotCommandsTexts);
        return commands;
    }

    public static void commandBuilder(List<TlgBotTranslationEntity> ru, List<TlgBotCommandsText> botCommandsTexts) {
        for (TlgBotTranslationEntity tlgBotTranslationEntity : ru) {
            TlgBotCommandsText tlgBotCommandsText = new TlgBotCommandsText();
            tlgBotCommandsText.setCommand(tlgBotTranslationEntity.getCommand());
            tlgBotCommandsText.setTranslatedCommand(tlgBotTranslationEntity.getTranslatedCommand());
            tlgBotCommandsText.setTranslatedCommandText(tlgBotTranslationEntity.getCommandText());
            tlgBotCommandsText.setStartEmoji(tlgBotTranslationEntity.getEmojiAtBegin());
            tlgBotCommandsText.setEndEmoji(tlgBotTranslationEntity.getEmojiAtEnd());
            botCommandsTexts.add(tlgBotCommandsText);
        }
    }
}
