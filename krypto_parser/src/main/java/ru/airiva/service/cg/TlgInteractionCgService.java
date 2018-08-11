package ru.airiva.service.cg;

import converter.TlgChannelToTlgChatEntityConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.airiva.entities.TlgChatEntity;
import ru.airiva.entities.TlgChatPairEntity;
import ru.airiva.entities.TlgClientEntity;
import ru.airiva.exception.*;
import ru.airiva.parser.Courier;
import ru.airiva.parser.Expression;
import ru.airiva.parser.Parser;
import ru.airiva.service.fg.KryptoParserInitializeFgService;
import ru.airiva.service.fg.TlgClientFgService;
import ru.airiva.service.fg.TlgInteractionFgService;
import ru.airiva.vo.TlgChannel;

import java.util.*;

@Service
public class TlgInteractionCgService implements TlgInteraction {

    private TlgInteractionFgService tlgInteractionFgService;
    private KryptoParserInitializeFgService kryptoParserInitializeFgService;
    private TlgClientFgService tlgClientFgService;
    private TlgChannelToTlgChatEntityConverter tlgChannelToTlgChatEntityConverter;

    @Autowired
    public void setTlgInteractionFgService(TlgInteractionFgService tlgInteractionFgService) {
        this.tlgInteractionFgService = tlgInteractionFgService;
    }

    @Autowired
    public void setKryptoParserInitializeFgService(KryptoParserInitializeFgService kryptoParserInitializeFgService) {
        this.kryptoParserInitializeFgService = kryptoParserInitializeFgService;
    }
    @Autowired
    public void setTlgClientFgService(TlgClientFgService tlgClientFgService) {
        this.tlgClientFgService = tlgClientFgService;
    }
    @Autowired
    public void setTlgChannelToTlgChatEntityConverter(TlgChannelToTlgChatEntityConverter tlgChannelToTlgChatEntityConverter) {
        this.tlgChannelToTlgChatEntityConverter = tlgChannelToTlgChatEntityConverter;
    }

    @Override
    public void authorize(final String phone) throws TlgWaitAuthCodeBsException, TlgFailAuthBsException, TlgDefaultBsException, TlgTimeoutBsException {
        try {
            tlgInteractionFgService.auth(phone);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TlgDefaultBsException(e);
        }
    }

    @Override
    public boolean checkCode(final String phone, final String code) throws TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException {
        boolean result;
        try {
            result = tlgInteractionFgService.checkCode(phone, code);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TlgDefaultBsException(e);
        }
        return result;
    }

    @Override
    public void startParsing(final String phone) throws TlgWaitAuthCodeBsException, TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException {

        try {
            tlgInteractionFgService.enableParsing(phone);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TlgDefaultBsException(e);
        }

    }

    @Override
    public void stopParsing(String phone) throws TlgWaitAuthCodeBsException, TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException {
        try {
            tlgInteractionFgService.disableParsing(phone);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TlgDefaultBsException(e);
        }
    }

    @Override
    public boolean isEnableParsing(String phone) {
        return false;
    }

    @Override
    public void logout(final String phone) {
        tlgInteractionFgService.logout(phone);
    }

    @Override
    public List<TlgChannel> getSortedChannels(String phone)
            throws TlgWaitAuthCodeBsException, TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException {
        List<TlgChannel> channels;
        try {
            channels = new ArrayList<>(tlgInteractionFgService.getChannels(phone));
            Collections.sort(channels);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TlgDefaultBsException(e);
        }
        return channels;
    }

    @Override
    public void includeParsing(String phone, long source, long target)
            throws TlgWaitAuthCodeBsException, TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException {
        TlgChatPairEntity pair = kryptoParserInitializeFgService.obtainEnabledTlgChatPair(phone, source, target);
        try {
            if (pair != null) {
                tlgInteractionFgService.addCourier(phone, new Courier(source, target, new Parser(pair.getOrderedExpressionEntities()), pair.getDelay()));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TlgDefaultBsException(e);
        }
    }

    @Override
    public void excludeParsing(String phone, long source, long target)
            throws TlgWaitAuthCodeBsException, TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException {
        try {
            tlgInteractionFgService.deleteCourier(phone, Courier.template(source, target));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TlgDefaultBsException(e);
        }
    }

    @Override
    public void setMessageSendingDelay(String phone, long source, long target, long delay)
            throws TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgDefaultBsException, TlgTimeoutBsException {
        try {
            tlgInteractionFgService.setCourierDelay(phone, Courier.template(source, target), delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TlgDefaultBsException(e);
        }
    }

    @Override
    public void addParsingExpression(String phone, long source, long target, String search, String replacement, int order)
            throws TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgDefaultBsException, TlgTimeoutBsException {
        try {
            tlgInteractionFgService.addExpression(phone, Courier.template(source, target), new Expression(search, replacement, order));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TlgDefaultBsException(e);
        }
    }

    @Override
    public void removeParsingExpression(String phone, long source, long target, String search, String replacement)
            throws TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgDefaultBsException, TlgTimeoutBsException {
        try {
            tlgInteractionFgService.removeExpression(phone, Courier.template(source, target), Expression.template(search, replacement));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TlgDefaultBsException(e);
        }
    }

    @Override
    public String resendCode(String phone) throws TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException {
        String codeType;
        try {
            codeType = tlgInteractionFgService.resendCode(phone);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new TlgDefaultBsException(e);
        }
        return codeType;
    }

    @Override
    public void addConsumer(String phone, String consumer) throws TlgDefaultBsException, TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgTimeoutBsException {
        List<TlgChannel> sortedChannels = getSortedChannels(phone);
        TlgChannel consChannel = sortedChannels.stream().filter(tlgChannel -> consumer.equals(tlgChannel.getChatId())).findFirst().get();
        consChannel.setConsumer(Boolean.TRUE);
        consChannel.setProducer(Boolean.FALSE);
        TlgClientEntity byPhone = tlgClientFgService.getByPhone(phone);
        TlgChatEntity chatFromEntity = byPhone.getOwnChats().stream().filter(tlgChatEntity -> consChannel.equals(tlgChatEntity.getTlgChatId())).findFirst().orElse(new TlgChatEntity());
        tlgChannelToTlgChatEntityConverter.convert(consChannel, chatFromEntity);
        tlgClientFgService.save(byPhone);
    }

    @Override
    public Set<TlgChannel> getAvailableChannel(String phone) throws TlgDefaultBsException, TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgTimeoutBsException {
        List<TlgChannel> sortedChannels = getSortedChannels(phone);
        Set<TlgChannel> tlgChannels = new HashSet<>(sortedChannels);
        TlgClientEntity byPhone = tlgClientFgService.getByPhone(phone);
        byPhone.getOwnChats().forEach(tlgChatEntity -> {
            TlgChannel channelToRemove = tlgChannels.stream().filter(tlgChannel -> tlgChatEntity.getTlgChatId().equals(tlgChannel.getChatId()) &&
                    (tlgChatEntity.getProducer() || tlgChatEntity.getConsumer())).findFirst().get();
            tlgChannels.remove(channelToRemove);
        });
        return tlgChannels;
    }
    @Override
    public void addProducer(String phone, String consumer) throws TlgDefaultBsException, TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgTimeoutBsException {
        List<TlgChannel> sortedChannels = getSortedChannels(phone);
        TlgChannel consChannel = sortedChannels.stream().filter(tlgChannel -> consumer.equals(tlgChannel.getChatId())).findFirst().get();
        consChannel.setConsumer(Boolean.FALSE);
        consChannel.setProducer(Boolean.TRUE);
        TlgClientEntity byPhone = tlgClientFgService.getByPhone(phone);
        TlgChatEntity chatFromEntity = byPhone.getOwnChats().stream().filter(tlgChatEntity -> consChannel.equals(tlgChatEntity.getTlgChatId())).findFirst().orElse(new TlgChatEntity());
        tlgChannelToTlgChatEntityConverter.convert(consChannel, chatFromEntity);
        tlgClientFgService.save(byPhone);
    }

    @Override
    public Set<TlgChatEntity> getConsumers(String phone) throws TlgDefaultBsException, TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgTimeoutBsException {
        List<TlgChannel> sortedChannels = getSortedChannels(phone);
        TlgClientEntity byPhone = tlgClientFgService.getByPhone(phone);
        Set<TlgChatEntity> ownChats = byPhone.getOwnChats();
        Set<TlgChatEntity> toRemove = new HashSet<>();
            for (TlgChatEntity ownChat : ownChats) {
                int count = 0;
                for (TlgChannel sortedChannel : sortedChannels) {
                    if (sortedChannel.getChatId() == ownChat.getTlgChatId()) {
                        count++;
                    }
                }
                if (count == 0) {
                    toRemove.add(ownChat);
                }
            }
        for (TlgChatEntity tlgChatEntity : toRemove) {
            byPhone.getOwnChats().remove(tlgChatEntity);
        }
        tlgClientFgService.save(byPhone);
        return byPhone.getOwnChats();
    }


}
