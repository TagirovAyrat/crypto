package ru.airiva.utils;

import org.reflections.Reflections;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import ru.airiva.annotations.BotContext;
import ru.airiva.annotations.BotContextMethod;
import ru.airiva.commands.CommandMarker;
import ru.airiva.entity.SessionData;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class ReflectionUtils {
    public static Class<? extends CommandMarker> findContextByCommand(String requestedCommand) {
        Reflections reflections = new Reflections("ru/airiva/commands");
        Set<Class<? extends CommandMarker>> allClasses =
                reflections.getSubTypesOf(CommandMarker.class);
        for (Class<? extends CommandMarker> commandClass : allClasses) {
            for (Annotation annotation : ((Class) commandClass).getDeclaredAnnotations()) {
                if (annotation.annotationType().getCanonicalName().equals(BotContext.class.getCanonicalName())) {
                    if (((BotContext) annotation).command().equals(requestedCommand)) {
                        return commandClass;
                    }
                }
            }
        }
        return null;
    }
    public static Method  findMethodByTextEquals(List<Method> methods, String text, SessionData sessionData) {
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
        List<Object> paramsToInject = new LinkedList<>();
        String tgId = null;
        try {
            for (Object param : sourceParams) {
                if (param.getClass().equals(Update.class)) {
                    Update up = (Update) param;
                    tgId = String.valueOf(MessageUtils.getTlgIdDependsOnUpdateType(up));
                    sourceParams.add(up);
                    break;
                }
            }
            for (Parameter targetParam : method.getParameters()) {
                for (Object sourceParam : sourceParams) {
                    if (targetParam.getType().isInstance(sourceParam)) {
                        paramsToInject.add(sourceParam);
                    }
                }
            }
            return (SendMessage) method.invoke(bean, paramsToInject);
        } catch (IllegalAccessException e) {
            return MessageUtils.sendDefaultErrorMessage(Integer.valueOf(tgId));
        } catch (InvocationTargetException e) {
            return MessageUtils.sendDefaultErrorMessage(Integer.valueOf(tgId));        }
    }

    public static void main(String[] args) {
        Locale locale = Locale.ENGLISH;
        System.out.println(locale.getLanguage());
        System.out.println(locale.getDisplayLanguage());
        System.out.println(locale.getDisplayName());
        System.out.println(Locale.forLanguageTag("ru"));

    }
}
