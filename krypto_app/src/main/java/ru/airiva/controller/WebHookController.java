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
import ru.airiva.utils.KeyboardUtils;

import java.io.IOException;

@RestController
@RequestMapping("/bot")
public class WebHookController {
    static {
        ApiContextInitializer.init();
    }
    private final Logger LOGGER = LoggerFactory.getLogger(WebHookController.class);

    private KryptoPrideWebHookBot kryptoPrideBot;
    private KryptoPrideLongPolingBot kryptoPrideLongPolingBot;
    @Autowired
    public void setKryptoPrideBot(KryptoPrideWebHookBot kryptoPrideBot) {
        this.kryptoPrideBot = kryptoPrideBot;
    }

    @Autowired
    public void setKryptoPrideLongPolingBot(KryptoPrideLongPolingBot kryptoPrideLongPolingBot) {
        this.kryptoPrideLongPolingBot = kryptoPrideLongPolingBot;
    }

    @PostMapping("/token/{token}")
    public ResponseEntity<BotApiMethod> updateReceived(@RequestPart("token") String token,
                                                       @RequestBody Update update) {
        BotApiMethod rs = null;
        try {
            rs = kryptoPrideBot.onWebhookUpdateReceived(update);
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
        } catch (TelegramApiRequestException e) {
            LOGGER.error("Register Long Polling bot error", e);
        }


    }

}
