package ru.airiva.dto.request;

import ru.airiva.uam.TlgBotCommandsText;

import java.util.List;

public class RegisterBotReq {

    List<TlgBotCommandsText> botCommandsTexts;

    public RegisterBotReq() {
    }

    public RegisterBotReq(List<TlgBotCommandsText> botCommandsTexts) {
        this.botCommandsTexts = botCommandsTexts;
    }

    public List<TlgBotCommandsText> getBotCommandsTexts() {
        return botCommandsTexts;
    }

    public void setBotCommandsTexts(List<TlgBotCommandsText> botCommandsTexts) {
        this.botCommandsTexts = botCommandsTexts;
    }
}
