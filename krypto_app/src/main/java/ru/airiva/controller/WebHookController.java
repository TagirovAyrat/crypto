package ru.airiva.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import ru.airiva.bot.KryptoPrideLongPolingBot;
import ru.airiva.bot.KryptoPrideWebHookBot;
import ru.airiva.properties.BotProperties;
import ru.airiva.utils.KeyboardUtils;

import java.io.IOException;

@RestController
public class WebHookController {
    static {
        ApiContextInitializer.init();
    }
    private final Logger LOGGER = LoggerFactory.getLogger(WebHookController.class);

    private KryptoPrideWebHookBot kryptoPrideBot;
    private BotProperties botProperties;
    private KryptoPrideLongPolingBot kryptoPrideLongPolingBot;
    @Autowired
    public void setKryptoPrideBot(KryptoPrideWebHookBot kryptoPrideBot) {
        this.kryptoPrideBot = kryptoPrideBot;
    }

    @Autowired
    public void setKryptoPrideLongPolingBot(KryptoPrideLongPolingBot kryptoPrideLongPolingBot) {
        this.kryptoPrideLongPolingBot = kryptoPrideLongPolingBot;
    }
    @Autowired
    public void setBotProperties(BotProperties botProperties) {
        this.botProperties = botProperties;
    }

    @PostMapping("/registerbot/{token}")
    public ResponseEntity<BotApiMethod> botRegister(@PathVariable String token ) {
        BotApiMethod rs = null;
        try {
            String url = "https://185.174.172.24:8443/kryptopride/" + token;
            String pathToPem = botProperties.pathToPem;
            kryptoPrideBot.setWebhook(url, pathToPem);
        } catch (Exception e) {
            return ResponseEntity.ok(rs);
        }
        return ResponseEntity.ok(rs);
    }

    @PostMapping("/{token}")
    public ResponseEntity<BotApiMethod> updateReceived(@PathVariable String token, @RequestBody Update update) {
        BotApiMethod rs = null;
        try {
            kryptoPrideBot.onWebhookUpdateReceived(update);
        } catch (Exception e) {
            return ResponseEntity.ok(rs);
        }
        return ResponseEntity.ok(rs);
    }

    @GetMapping("/longpolling")
    public void longPollingUpdateReceived()  {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(kryptoPrideLongPolingBot);
            kryptoPrideLongPolingBot.clearWebhook();
        } catch (TelegramApiRequestException e) {
            LOGGER.error("Register Long Polling bot error", e);
        }
    }

}
