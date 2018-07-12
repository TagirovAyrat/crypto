package ru.airiva.exception;

public class TlgBotRegisterException extends  BsException{
    public TlgBotRegisterException(String message) {
        super(message == null ? "Ошибка регистрации бота" : message);
    }
}
