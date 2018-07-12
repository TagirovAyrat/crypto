package ru.airiva.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.airiva.bot.KryptoPrideWebHookBot;
import ru.airiva.entities.PersonEntity;
import ru.airiva.entities.TlgBotEntity;
import ru.airiva.exception.TlgBotRegisterException;
import ru.airiva.mappers.TlgBotMapper;
import ru.airiva.service.fg.KryptoBotService;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KryptoBotModuleService {

    private KryptoBotService kryptoBotService;
    private TlgBotMapper tlgBotMapper;

    @Autowired
    public void setKryptoBotService(KryptoBotService kryptoBotService) {
        this.kryptoBotService = kryptoBotService;
    }
    @Autowired
    public void setTlgBotMapper(TlgBotMapper tlgBotMapper) {
        this.tlgBotMapper = tlgBotMapper;
    }

    public ConcurrentHashMap<String, KryptoPrideWebHookBot> fillMapWithBotsFromDB() {
        ConcurrentHashMap<String, KryptoPrideWebHookBot> stringKryptoPrideWebHookBotMap = null;
        List<TlgBotEntity> tlgBotEntities = kryptoBotService.allRegistredBots();
        if (tlgBotEntities != null &&!tlgBotEntities.isEmpty()) {
            stringKryptoPrideWebHookBotMap = tlgBotMapper.mapperFromTlgBotToWebHookBot(tlgBotEntities);
        }
        return stringKryptoPrideWebHookBotMap;
    }


    public void saveTlgBot(String personId, KryptoPrideWebHookBot kryptoPrideWebHookBot) throws TlgBotRegisterException {
        if (kryptoBotService.isBotAlredyExist(kryptoPrideWebHookBot.getBotToken())) {
            throw new TlgBotRegisterException("Данный бот уже зарегистрирован");
        }
        PersonEntity personById = kryptoBotService.findPersonById(Long.valueOf(personId));
        if (personById == null) {
            throw new TlgBotRegisterException("Указанный пользователь не существует");
        }
        TlgBotEntity tlgBotEntity = tlgBotMapper.mapperFromWebHookBotToTlgBotEntity(kryptoPrideWebHookBot.getBotToken(),
                personById, kryptoPrideWebHookBot.getBotUsername());
        if (!kryptoBotService.saveTlgBot(tlgBotEntity).isPresent()) {
            throw new TlgBotRegisterException("Не удалось сохранить бота");
        }
    }

}
