package ru.airiva.converter;

import org.springframework.stereotype.Component;
import ru.airiva.entities.TlgChatEntity;
import ru.airiva.vo.TlgChannel;
@Component
public class TlgChannelToEntityConverter implements Converter<TlgChatEntity, TlgChannel>{

    @Override
    public void convert(TlgChannel source, TlgChatEntity dest) {
        dest.setTlgChatId(source.getChatId());
        dest.setUsername(source.getUsername());
        dest.setTitle(source.getTitle());
        dest.setConsumer(source.getConsumer());
        dest.setProducer(source.getProducer());
    }
}
