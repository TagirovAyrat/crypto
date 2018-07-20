package ru.airiva.service.fg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.airiva.entities.PersonEntity;
import ru.airiva.entities.TlgBotCommandsEntity;
import ru.airiva.entities.TlgBotEntity;
import ru.airiva.entities.TlgBotTranslationEntity;
import ru.airiva.service.da.repository.PersonRepo;
import ru.airiva.service.da.repository.TlgBotCommandsRepo;
import ru.airiva.service.da.repository.TlgBotRepo;
import ru.airiva.service.da.repository.TlgBotTranslationRepo;

import java.util.*;

@Service
public class KryptoBotService {

    private TlgBotRepo tlgBotRepo;
    private PersonRepo personRepo;
    private TlgBotTranslationRepo tlgBotTranslationRepo;
    private TlgBotCommandsRepo tlgBotCommandsRepo;

    @Autowired
    public void setTlgBotRepo(TlgBotRepo tlgBotRepo) {
        this.tlgBotRepo = tlgBotRepo;
    }

    @Autowired
    public void setPersonRepo(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @Autowired
    public void setTlgBotTranslationRepo(TlgBotTranslationRepo tlgBotTranslationRepo) {
        this.tlgBotTranslationRepo = tlgBotTranslationRepo;
    }
    @Autowired
    public void setTlgBotCommandsRepo(TlgBotCommandsRepo tlgBotCommandsRepo) {
        this.tlgBotCommandsRepo = tlgBotCommandsRepo;
    }

    public List<TlgBotEntity> allRegistredBots() {
        return tlgBotRepo.findAll();
    }

    public Boolean isBotAlredyExist(String token) {
        return tlgBotRepo.findByToken(token) != null ? Boolean.TRUE: Boolean.FALSE;
    }

    public PersonEntity findPersonById(Long id) {
        return personRepo.findById(id).orElse(null);
    }
    @Transactional
    public Optional<TlgBotEntity> saveTlgBot(TlgBotEntity tlgBotEntity) {
        return Optional.ofNullable(tlgBotRepo.saveAndFlush(tlgBotEntity));
    }


    public Set<TlgBotTranslationEntity> commandTextFromDB() {
        return new HashSet<>(tlgBotTranslationRepo.findAll());
    }

    public List<String> getCommandList() {
        List<String> commands = new ArrayList<>();
        tlgBotCommandsRepo.findAll().forEach(tlgBotCommandsEntity -> commands.add(tlgBotCommandsEntity.getCommandName()));
        return commands;
    }
}
