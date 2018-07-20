package ru.airiva.uam;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
@JsonInclude
public class TlgBotCommandsText implements Serializable {
    //Локаль
    private String locale;
    //Команда
    private String command;
    //Команда на языке локали
    private String translatedCommand;
    //Текст команды на языке локали
    private String translatedCommandText;
    //emoji at start
    private String startEmoji;
    //emoji at end
    private String endEmoji;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getTranslatedCommand() {
        return translatedCommand;
    }

    public void setTranslatedCommand(String translatedCommand) {
        this.translatedCommand = translatedCommand;
    }

    public String getStartEmoji() {
        return startEmoji;
    }

    public void setStartEmoji(String startEmoji) {
        this.startEmoji = startEmoji;
    }

    public String getEndEmoji() {
        return endEmoji;
    }

    public void setEndEmoji(String endEmoji) {
        this.endEmoji = endEmoji;
    }

    public String getTranslatedCommandText() {
        return translatedCommandText;
    }

    public void setTranslatedCommandText(String translatedCommandText) {
        this.translatedCommandText = translatedCommandText;
    }
}
