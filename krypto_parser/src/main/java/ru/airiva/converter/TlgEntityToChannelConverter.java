package ru.airiva.converter;

import org.springframework.stereotype.Component;
import ru.airiva.entities.TlgChatEntity;
import ru.airiva.vo.TlgChannel;
@Component
public class TlgEntityToChannelConverter implements Converter<TlgChannel, TlgChatEntity> {
    @Override
    public void convert(TlgChatEntity source, TlgChannel dest) {
        dest.setChatId(source.getTlgChatId());
        dest.setUsername(source.getUsername());
        dest.setTitle(source.getTitle());
        dest.setConsumer(source.getConsumer());
        dest.setProducer(source.getProducer());
    }
}
