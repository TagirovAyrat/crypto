package ru.airiva.handler;

import org.springframework.stereotype.Component;
import ru.airiva.bot.KryptoPrideWebHookBot;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class BotsMapHandler {
    private ConcurrentHashMap<String, KryptoPrideWebHookBot> botList;

    public ConcurrentHashMap<String, KryptoPrideWebHookBot> getBotList() {
        if (botList == null) {
            return new ConcurrentHashMap<>();
        } else {
            return botList;
        }
    }

    public void setBotList(ConcurrentHashMap<String, KryptoPrideWebHookBot> botList) {
        this.botList = botList;
    }
}
