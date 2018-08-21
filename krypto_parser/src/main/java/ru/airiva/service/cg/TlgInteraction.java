package ru.airiva.service.cg;

import ru.airiva.exception.*;
import ru.airiva.vo.TlgChannel;

import java.util.List;
import java.util.Set;

public interface TlgInteraction {

    /**
     * Авторизация клиента
     *
     * @param phone телефон клиента
     */
    void authorize(String phone) throws TlgWaitAuthCodeBsException, TlgFailAuthBsException, TlgDefaultBsException, TlgTimeoutBsException;

    /**
     * Запуск парсинга для текущего клиента
     *
     * @param phone телефон клиента
     */
    void startParsing(String phone) throws TlgWaitAuthCodeBsException, TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException;

    /**
     * Остановка парсинга для текущего клиента
     *
     * @param phone телефон клиента
     */
    void stopParsing(String phone) throws TlgWaitAuthCodeBsException, TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException;

    /**
     * Статус активности парсинга текущего клиента
     *
     * @param phone телефон клиента
     * @return true - парсинг активен, иначе - false
     */
    boolean isEnableParsing(String phone);

    /**
     * Проверка кода аутентификации клиента
     *
     * @param code  код
     * @param phone телефон клиента
     * @return результат проверки
     */
    boolean checkCode(String phone, String code) throws TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException;

    /**
     * Логоут клиента
     *
     * @param phone телефон клиента
     */
    void logout(String phone);

    /**
     * Отсортированный по названию список аналов клиента
     *
     * @param phone телефон клиента
     */
    List<TlgChannel> getSortedChannels(String phone)
            throws TlgWaitAuthCodeBsException, TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException;

    /**
     * Добавление парсинга из канала {@code source} в канал {@code target}
     *
     * @param phone  телефон клиента
     * @param source идентификатор канала источника
     * @param target идентификатор канала потребителя
     */
    void includeParsing(String phone, long source, long target)
            throws TlgWaitAuthCodeBsException, TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException;

    /**
     * Исключение парсинга из канала {@code source} в канал {@code target}
     *
     * @param phone  телефон клиента
     * @param source идентификатор канала источника
     * @param target идентификатор канала потребителя
     */
    void excludeParsing(String phone, long source, long target)
            throws TlgWaitAuthCodeBsException, TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException;

    /**
     * Изменение задержки отправки сообщения из канала {@code source} в канал {@code target}
     *
     * @param phone  телефон клиента
     * @param source идентификатор канала источника
     * @param target идентификатор канала потребителя
     * @param delay  задержка отправки сообщения
     */
    void setMessageSendingDelay(String phone, long source, long target, long delay)
            throws TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgDefaultBsException, TlgTimeoutBsException;

    /**
     * Добавление шаблона для парсинга из канала-источника в канал-потребитель для текущего клиента
     *
     * @param phone       телефон текущего клиента
     * @param source      идентификатор канала-источника
     * @param target      идентификатор канала-потребителя
     * @param search      шаблон для поиска
     * @param replacement шаблон для замены
     * @param order       позиция в списке шаблонов курьера
     */
    void addParsingExpression(String phone, long source, long target, String search, String replacement, int order)
            throws TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgDefaultBsException, TlgTimeoutBsException;

    /**
     * Удаление шаблона для парсинга из канала-источника в канал-потребитель для текущего клиента
     *
     * @param phone       телефон текущего клиента
     * @param source      идентификатор канала-источника
     * @param target      идентификатор канала-потребителя
     * @param search      шаблон для поиска
     * @param replacement шаблон для замены
     */
    void removeParsingExpression(String phone, long source, long target, String search, String replacement)
            throws TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgDefaultBsException, TlgTimeoutBsException;

    /**
     * Повторная отправка кода аутентификации
     *
     * @param phone телефон клиента
     * @return тип кода (смс, чат)
     */
    String resendCode(String phone) throws TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException;

    /**
     * Добавление потребителя
     * @param phone - номер телефона клиента
     * @param consumers - канал для добавления
     */
    void addConsumer(String phone, String consumers) throws TlgDefaultBsException, TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgTimeoutBsException;

    /**
     * Получение списка каналов, которые не являются ни потребителями ни источниками
     * @param phone - телефон клоиента
     * @return
     */
    Set<TlgChannel> getAvailableChannel(String phone) throws TlgDefaultBsException, TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgTimeoutBsException;

    /**
     * Добавление источника
     * @param phone - номер телефона клиента
     * @param consumer - канал для добавления
     */
    void addProducer(String phone, String consumer) throws TlgDefaultBsException, TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgTimeoutBsException;

    /**
     *  Получение списка потребителей
     * @param phone - телефон клиента
     * @return
     */
    Set<TlgChannel> getConsumers(String phone) throws TlgDefaultBsException, TlgNeedAuthBsException, TlgWaitAuthCodeBsException, TlgTimeoutBsException;

    /**
     * Удаление потребителя
     * @param phone - телефон клиента
     * @param chatId - ID чата
     */
    void delConsumer(String phone, Long chatId);

    /**
     * Получение списка источников
     * @param phone - телефон клиента
     * @return
     * @throws TlgNeedAuthBsException
     * @throws TlgDefaultBsException
     * @throws TlgTimeoutBsException
     * @throws TlgWaitAuthCodeBsException
     */
    Set<TlgChannel> getProducers(String phone) throws TlgNeedAuthBsException, TlgDefaultBsException, TlgTimeoutBsException, TlgWaitAuthCodeBsException;

    /**
     * Удаление источника
     * @param phone - телефон клиента
     * @param chatId - id канала
     */
    void delProducer(String phone, Long chatId);
}
