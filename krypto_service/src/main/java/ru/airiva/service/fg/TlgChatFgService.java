package ru.airiva.service.fg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.airiva.entities.TlgChatEntity;
import ru.airiva.entities.TlgClientEntity;
import ru.airiva.exception.BsException;
import ru.airiva.service.da.repository.TlgChatRepo;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author Ivan
 */
@Service
public class TlgChatFgService {

    private TlgChatRepo tlgChatRepo;
    private TlgClientFgService tlgClientFgService;
    private static final Logger logger = LoggerFactory.getLogger(TlgChatFgService.class);

    @Autowired
    public void setTlgChatRepo(TlgChatRepo tlgChatRepo) {
        this.tlgChatRepo = tlgChatRepo;
    }
    @Autowired
    public void setTlgClientFgService(TlgClientFgService tlgClientFgService) {
        this.tlgClientFgService = tlgClientFgService;
    }

    public TlgChatEntity getById(Long chatId) {
        return tlgChatRepo.findById(chatId).orElse(null);
    }

    public TlgChatEntity saveChat(TlgChatEntity tlgChatEntity) {
        return tlgChatRepo.saveAndFlush(tlgChatEntity);
    }

    public void addConsumers(Optional<String> phone, List<Long> consumers) throws BsException {
        if (!phone.isPresent()) {
            throw new BsException("Телефон клиента пуст");
        }
        if (consumers.isEmpty()) {
            throw new BsException("Список потребителей пуст");
        }

        TlgClientEntity byPhone = tlgClientFgService.getByPhone(phone.get());
        try {
            consumers.forEach(aLong -> {
                byPhone.getOwnChats().stream().filter(tlgChatEntity -> tlgChatEntity.getTlgChatId().equals(aLong)).findFirst().get().setConsumer(Boolean.TRUE);
            });
        } catch (NoSuchElementException e) {
            throw new BsException("Прислан не корректный список каналов");
        }
    }

    public void addProducer(Optional<String> phone, List<Long> producers) throws BsException {
        if (!phone.isPresent()) {
            throw new BsException("Телефон клиента пуст");
        }
        if (producers.isEmpty()) {
            throw new BsException("Список потребителей пуст");
        }
        TlgClientEntity byPhone = tlgClientFgService.getByPhone(phone.get());
        try {
            producers.forEach(aLong -> {
                byPhone.getOwnChats().stream().filter(tlgChatEntity -> tlgChatEntity.getTlgChatId().equals(aLong)).findFirst().get().setProducer(Boolean.TRUE);
            });
        } catch (NoSuchElementException e) {
            throw new BsException("Прислан не корректный список каналов");
        }
    }

}
