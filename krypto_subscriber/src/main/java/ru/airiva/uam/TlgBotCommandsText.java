package ru.airiva.uam;

import java.io.Serializable;

public class TlgBotCommandsText implements Serializable {
    //Локаль
    private String locale;
    //Команда
    private String command;
    //Текст который видит пользователь при нажатии на команду на языке локали
    private String commandText;
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

    public String getCommandText() {
        return commandText;
    }

    public void setCommandText(String commandText) {
        this.commandText = commandText;
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
}
