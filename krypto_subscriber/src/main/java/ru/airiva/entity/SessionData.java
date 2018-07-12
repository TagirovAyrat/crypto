package ru.airiva.entity;

import ru.airiva.uam.TlgBotCommandsText;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class SessionData implements Serializable {
    private String locale;
    private String tlgId;
    private String currentStep;
    private String currentCommand;
    private List<TlgBotCommandsText> tlgBotCommandsTexts;

    public SessionData(String locale, String tlgId, String currentStep, String currentCommand, List<TlgBotCommandsText> tlgBotCommandsTexts) {
        this.locale = locale;
        this.tlgId = tlgId;
        this.currentStep = currentStep;
        this.currentCommand = currentCommand;
        this.tlgBotCommandsTexts = tlgBotCommandsTexts;
    }

    public SessionData() {
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTlgId() {
        return tlgId;
    }

    public void setTlgId(String tlgId) {
        this.tlgId = tlgId;
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

    public List<TlgBotCommandsText> getTlgBotCommandsTexts() {
        return tlgBotCommandsTexts;
    }

    public void setTlgBotCommandsTexts(List<TlgBotCommandsText> tlgBotCommandsTexts) {
        this.tlgBotCommandsTexts = tlgBotCommandsTexts;
    }
}
