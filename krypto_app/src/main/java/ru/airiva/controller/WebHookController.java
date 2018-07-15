package ru.airiva.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import ru.airiva.bot.KryptoPrideWebHookBot;
import ru.airiva.dto.ResponseDTO;
import ru.airiva.exception.BsException;
import ru.airiva.handler.BotsMapHandler;
import ru.airiva.properties.BotProperties;
import ru.airiva.service.KryptoBotModuleService;
import ru.airiva.uam.TlgBotCommandsText;
import ru.airiva.utils.KeyboardUtils;
import ru.airiva.utils.SpringContext;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@RestController
public class WebHookController {

    public static final String ERROR = "-----------------------------ERROR-------------------------------";
    public static final String HASHMAPISNULL = "На момент получения запроса от telegram HASHMAP не инициализирована";
    public static final String NOTOKENINMAP = "----- HASHMAP! не содержит указанного бота-----";

    static {
        ApiContextInitializer.init();
    }

    private final Logger LOGGER = LoggerFactory.getLogger(WebHookController.class);

    private BotProperties botProperties;
    private KryptoBotModuleService kryptoBotModuleService;
    private BotsMapHandler botsMapHandler;

    @Autowired
    public void setBotProperties(BotProperties botProperties) {
        this.botProperties = botProperties;
    }

    @Autowired
    public void setKryptoBotModuleService(KryptoBotModuleService kryptoBotModuleService) {
        this.kryptoBotModuleService = kryptoBotModuleService;
    }

    @Autowired
    public void setBotsMapHandler(BotsMapHandler botsMapHandler) {
        this.botsMapHandler = botsMapHandler;
    }

    /**
     * Заполнение мапы с ботами при старте приложения из базы данных
     */
    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.debug("Заполнение MAP из базы данных");
        botsMapHandler.setBotList(kryptoBotModuleService.fillMapWithBotsFromDB());
    }

    /**
     * Метод регистрации бота
     *
     * @param token    - токен бота.
     * @param personId - Id пользователя консоли.
     * @param username - username бота.
     * @return ответ на фронт с текстом ошибки или HTTP.OK если все хорошо.
     */

    @PostMapping(value = "/registerbot/{token}/{personId}/{username}", produces = APPLICATION_JSON, consumes = APPLICATION_JSON)
    public ResponseEntity<ResponseDTO> botRegister(@PathVariable("token") String token, @PathVariable("personId") String personId,
                                                   @PathVariable("username") String username, @RequestBody ArrayList<TlgBotCommandsText> commands) {
            try {
            LOGGER.debug("Register bot with token = " + token + " personId = " + personId + " username = " + username);
            KryptoPrideWebHookBot kryptoPrideWebHookBot = SpringContext.getContext().getBean(KryptoPrideWebHookBot.class);
            kryptoPrideWebHookBot.setToken(token);
            kryptoPrideWebHookBot.setUserName(username);
            kryptoPrideWebHookBot.setTlgBotCommandsText(KeyboardUtils.parseCommandsFromJson(commands));
            kryptoBotModuleService.saveTlgBot(personId, kryptoPrideWebHookBot);

            botsMapHandler.getBotList().put(token, kryptoPrideWebHookBot);
            String url = botProperties.externalUrl + token;
            String pathToPem = botProperties.pathToPem;
            kryptoPrideWebHookBot.setWebhook(url, pathToPem);
            LOGGER.debug("bot with token = " + token + " personId = " + personId + " username = " + username + " REGISTRED");
        } catch (BsException e) {
            return ResponseEntity.ok(new ResponseDTO(e));
        } catch (TelegramApiRequestException e) {
            return ResponseEntity.ok(new ResponseDTO(new BsException(e.getLocalizedMessage())));
        }
        return ResponseEntity.ok(new ResponseDTO());
    }

//    @PostMapping(value = "/registerbot/{token}/{personId}/{username}", produces = APPLICATION_JSON, consumes = APPLICATION_JSON)
//    public ResponseEntity<ResponseDTO> botRegister(@PathVariable("token") String token, @PathVariable("personId") String personId,
//                                                   @PathVariable("username") String username) {
//            try {
//            LOGGER.debug("Register bot with token = " + token + " personId = " + personId + " username = " + username);
//            KryptoPrideWebHookBot kryptoPrideWebHookBot = SpringContext.getContext().getBean(KryptoPrideWebHookBot.class);
//            kryptoPrideWebHookBot.setToken(token);
//            kryptoPrideWebHookBot.setUserName(username);
//            kryptoPrideWebHookBot.setTlgBotCommandsText(KeyboardUtils.parseCommandsFromJson(commands));
//            kryptoBotModuleService.saveTlgBot(personId, kryptoPrideWebHookBot);
//
//            botsMapHandler.getBotList().put(token, kryptoPrideWebHookBot);
//            String url = botProperties.externalUrl + token;
//            String pathToPem = botProperties.pathToPem;
//            kryptoPrideWebHookBot.setWebhook(url, pathToPem);
//            LOGGER.debug("bot with token = " + token + " personId = " + personId + " username = " + username + " REGISTRED");
//        } catch (BsException e) {
//            return ResponseEntity.ok(new ResponseDTO(e));
//        } catch (TelegramApiRequestException e) {
//            return ResponseEntity.ok(new ResponseDTO(new BsException(e.getLocalizedMessage())));
//        }
//        return ResponseEntity.ok(new ResponseDTO());
//    }


    /**
     * Контроллер который регистрируется в Telegram и на который приходят обновления
     *
     * @param token  - токен бота
     * @param update - обновление которое пришло из бота
     * @return
     */
    @PostMapping("/{token}")
    public Response updateReceived(@PathVariable String token, @RequestBody Update update) {
        try {

            if (botsMapHandler.getBotList() == null) {
                LOGGER.error(ERROR);
                LOGGER.error(HASHMAPISNULL);
                return Response.status(Response.Status.OK).build();
            }
            KryptoPrideWebHookBot kryptoPrideWebHookBot = botsMapHandler.getBotList().get(token);
            if (kryptoPrideWebHookBot == null) {
                LOGGER.error(ERROR);
                LOGGER.error(NOTOKENINMAP);
                return Response.status(Response.Status.OK).build();
            }
            BotApiMethod response = kryptoPrideWebHookBot.onWebhookUpdateReceived(update);
            return Response.ok(response).build();
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage());
        }
        return Response.status(Response.Status.OK).build();
    }

}
