package ru.airiva.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.airiva.bot.KryptoPrideWebHookBot;
import ru.airiva.commands.CancelCommand;
import ru.airiva.commands.CommandMarker;
import ru.airiva.entity.SessionData;
import ru.airiva.enums.CommandList;
import ru.airiva.repo.AreaRedisRepo;
import ru.airiva.utils.MessageUtils;
import ru.airiva.utils.ReflectionUtils;
import ru.airiva.utils.SpringContext;

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
    public BotApiMethod handle(KryptoPrideWebHookBot kryptoPrideWebHookBot, Update update) {
        SendMessage sendMessage;
        String currentStep = null;
        List<Method> methodsByStep ;
        Method methodToRun = null;
        Class<? extends CommandMarker> commandClass;

        //Получаем tgId из апдейта и текущее сообщение
        Integer id = update.getMessage().getFrom().getId();
        String message = update.getMessage().getText();
        //Получаем текущий контекст
        SessionData sessionData = areaRedisRepo.get(String.valueOf(id));

        //Убираем слеш из сообщения
        String inputCommand = MessageUtils.extractCommandFromMessage(kryptoPrideWebHookBot, message);
        if (inputCommand.equalsIgnoreCase(CommandList.CANCEL.name())) {
            CancelCommand bean = SpringContext.getContext().getBean(CancelCommand.class);
            return bean.initial(update);
        }
        if (sessionData == null) {
            commandClass = ReflectionUtils.findContextByCommand(inputCommand.toUpperCase());
        } else {
            commandClass = ReflectionUtils.findContextByCommand(sessionData.getCurrentCommand());
            currentStep = sessionData.getCurrentStep();
        }
        try {
            if (currentStep != null) {
                //Если команда состоит из шагов то ищем текущий шаг
                methodsByStep = ReflectionUtils.getMethodsByStep(commandClass, currentStep);
                if (methodsByStep != null ) {
                    //Если вариант только один, то мы нашли наш метод
                    if (methodsByStep.size() == 1) {
                        methodToRun = methodsByStep.get(0);
                    } else if (methodsByStep.size() > 1) {
                        //Если методов несколько то ищем по введенному тексту
                            methodToRun = ReflectionUtils.findMethodByTextEquals(methodsByStep, inputCommand);
                    }
                }
            }else {
                //Ищем по введенному тексту, если не нашли то ищем метод по умолчанию
                methodToRun = ReflectionUtils.findMethodByTextEqualsInCommandClass(commandClass, inputCommand);
                //Если класс найден, но нет шага, то выполняем метод по умолчанию - initial
                if (methodToRun == null) {
                    methodToRun = ReflectionUtils.getMethodsByCommand(commandClass);
                }
            }
        } catch (IllegalArgumentException e) {
            MessageUtils.sendDefaultErrorMessage(id);
        }
        if (inputCommand == null || methodToRun == null || commandClass == null )  {
            return MessageUtils.unknownOperationResponse(sessionData, id);
        }
        CommandMarker bean = SpringContext.getContext().getBean(commandClass);
        sendMessage = ReflectionUtils.invokeMethod(methodToRun, bean, update, kryptoPrideWebHookBot);
        return sendMessage;
    }



}
