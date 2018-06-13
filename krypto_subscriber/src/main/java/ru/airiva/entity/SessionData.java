package ru.airiva.entity;

import java.util.Locale;

public class SessionData {
    private String chatId;
    private Locale locale;
    private String currentStep;
    private String currentCommand;

    public SessionData(String chatId, Locale locale, String currentStep, String currentCommand) {
        this.chatId = chatId;
        this.locale = locale;
        this.currentStep = currentStep;
        this.currentCommand = currentCommand;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(String currentStep) {
        this.currentStep = currentStep;
    }

    public String getCurrentCommand() {
        return currentCommand;
    }

    public void setCurrentCommand(String currentCommand) {
        this.currentCommand = currentCommand;
    }
}
