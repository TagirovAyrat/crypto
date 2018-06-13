package ru.airiva.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.airiva.commands.CommandMarker;
import ru.airiva.entity.SessionData;
import ru.airiva.enums.CommandList;
import ru.airiva.repo.AreaRedisRepo;
import ru.airiva.utils.MessageUtils;
import ru.airiva.utils.ReflectionUtils;
import ru.airiva.utils.SpringContextProvider;

import java.lang.reflect.Method;
import java.util.List;


/**
 * Класс который принимает текстовые обновления от бота
 * метед handle содержит в себе логику обработки запроса.
 */
@Component
public class MessageHandler implements UpdateHandler {
    public static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);
    private AreaRedisRepo areaRedisRepo;

    @Autowired
    public void setAreaRedisRepo(AreaRedisRepo areaRedisRepo) {
        this.areaRedisRepo = areaRedisRepo;
    }

    @Override
    public BotApiMethod handle(Update update) {
        SendMessage sendMessage;
        String message;
        List<Method> methodsByStep = null;
        Method methodToRun = null;
        message = update.getMessage().getText();
        Integer id = update.getMessage().getFrom().getId();
        SessionData sessionData = areaRedisRepo.get(String.valueOf(id));
        String currentStep = sessionData.getCurrentStep();
        String inputCommand = MessageUtils.extractCommandFromMessage(message).toUpperCase();
        String command;
        //Смотрим есть ли у нас такая команда в боте
        try {
            command = CommandList.valueOf(inputCommand).toString();
        } catch (IllegalArgumentException e) {
            return MessageUtils.unknownOperationResponse(sessionData, id);
        }
        //Если есть то находим класс который отвечает за эту команду
        Class<? extends CommandMarker> commandClass = ReflectionUtils.findContextByCommand(command);

        //Если команда состоит из шагов то ищем текущий шаг
        if (currentStep != null) {
            methodsByStep = ReflectionUtils.getMethodsByStep(commandClass, currentStep);
            if (methodsByStep != null ) {
                //Если вариант только один, то мы нашли наш метод
                if (methodsByStep.size() == 1) {
                    methodToRun = methodsByStep.get(0);
                } else if (methodsByStep.size() > 1) {
                    //Если методов несколько то ищем по введенному тексту
                    methodToRun = ReflectionUtils.findMethodByTextEquals(methodsByStep, command, sessionData);
                }
            }
        }else {
            //Если класс найден, но нет шага, то выполняем метод по умолчанию - initial
            methodToRun = ReflectionUtils.getMethodsByCommand(commandClass);
        }
        CommandMarker bean = SpringContextProvider.getApplicationContext().getBean(commandClass);
        sendMessage = ReflectionUtils.invokeMethod(methodToRun, bean);
        return sendMessage;
    }







}
