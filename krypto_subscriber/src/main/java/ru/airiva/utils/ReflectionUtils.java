package ru.airiva.utils;

import org.reflections.Reflections;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.airiva.annotations.BotContext;
import ru.airiva.annotations.BotContextMethod;
import ru.airiva.bot.KryptoPrideWebHookBot;
import ru.airiva.commands.CommandMarker;
import ru.airiva.commands.StartCommand;
import ru.airiva.entity.SessionData;
import ru.airiva.enums.CommandList;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Component
public class ReflectionUtils {
    public static Class<? extends CommandMarker> findContextByCommand(String requestedCommand) {
        Reflections reflections = new Reflections("ru/airiva/commands");
        Set<Class<? extends CommandMarker>> allClasses = reflections.getSubTypesOf(CommandMarker.class);
        for (Class<? extends CommandMarker> commandClass : allClasses) {
            for (Annotation annotation : ((Class) commandClass).getDeclaredAnnotations()) {
                if (annotation.annotationType().getCanonicalName().equals(BotContext.class.getCanonicalName())) {
                    if (((BotContext) annotation).command().name().equalsIgnoreCase(requestedCommand)) {
                        return commandClass;
                    }
                }
            }
        }
        return null;
    }

    public static Method findMethodByTextEqualsInCommandClass(Class<? extends CommandMarker> commandClass, String command) throws IllegalArgumentException {
        List<Method> result = new ArrayList<>();
        for (Method method : commandClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BotContextMethod.class)) {
                BotContextMethod declaredAnnotation = method.getDeclaredAnnotation(BotContextMethod.class);
                for (String inputText : declaredAnnotation.ifInputTextEquals()) {
                    if (inputText.equalsIgnoreCase(command)) {
                        result.add(method);
                    }
                }
            }
        }
        if (result.size() > 1) {
            throw new IllegalArgumentException();
        }
        return result.size() == 0 ? null : result.get(0);
    }

    public static Method findMethodByTextEquals(List<Method> methods, String text) throws IllegalArgumentException {
        List<Method> result = new ArrayList<>();
        for (Method method : methods) {
            BotContextMethod declaredAnnotation = method.getDeclaredAnnotation(BotContextMethod.class);
            for (String inputText : declaredAnnotation.ifInputTextEquals()) {
                if (inputText.equalsIgnoreCase(text)) {
                    result.add(method);
                }
            }
        }
        if (result.size() != 1) {
            throw new IllegalArgumentException();
        }
        return result.get(0);
    }

    public static List<Method> getMethodsByStep(Class<? extends CommandMarker> context, String stepName) {
        List<Method> methods = new ArrayList<>();
        for (Method method : context.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BotContextMethod.class)) {
                BotContextMethod declaredAnnotation = method.getDeclaredAnnotation(BotContextMethod.class);
                if (declaredAnnotation.step().equals(stepName)) {
                    methods.add(method);
                }

            }
        }
        return methods;
    }

    public static Method getMethodsByCommand(Class<? extends CommandMarker> context) {
        for (Method method : context.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BotContextMethod.class)) {
                BotContextMethod declaredAnnotation = method.getDeclaredAnnotation(BotContextMethod.class);
                if (declaredAnnotation.step().equals("initial")) {
                    return method;
                }
            }
        }
        return null;
    }

    public static SendMessage invokeMethod(Method method, CommandMarker bean, Object... params) {
        List<Object> sourceParams = new ArrayList<>(Arrays.asList(params));
        List<Object> updateParams = new ArrayList<>();
        List<Object> paramsToInject = new LinkedList<>();
        String tgId = null;
        try {
            for (Object param : sourceParams) {
                if (param.getClass().equals(Update.class)) {
                    Update up = (Update) param;
                    tgId = String.valueOf(MessageUtils.getTlgIdDependsOnUpdateType(up));
                    updateParams.add(up);
                }
                if (param.getClass().equals(KryptoPrideWebHookBot.class)) {
                    KryptoPrideWebHookBot hookBot = (KryptoPrideWebHookBot) param;
                    updateParams.add(hookBot);
                }

            }
            for (Parameter targetParam : method.getParameters()) {
                for (Object sourceParam : updateParams) {
                    if (targetParam.getType().isInstance(sourceParam)) {
                        paramsToInject.add(sourceParam);
                    }
                }
            }
            return (SendMessage) method.invoke(bean, paramsToInject.toArray());
        } catch (IllegalAccessException e) {
            return MessageUtils.sendDefaultErrorMessage(Integer.valueOf(tgId));
        } catch (InvocationTargetException e) {
            return MessageUtils.sendDefaultErrorMessage(Integer.valueOf(tgId));
        }
    }


}
