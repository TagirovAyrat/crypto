package ru.airiva.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static ru.airiva.entities.EntityConstants.TLG_CHATS;

/**
 * @author Ivan
 */
@Entity
@Table(name = TLG_CHATS)
public class TlgChatEntity {

    @Id
    @Column(name = "tlg_chat_id")
    private Long tlgChatId;

    @Column(name = "title")
    private String title;

    @Column(name = "username")
    private String username;

    @Column(name = "is_channel")
    private boolean channel = false;

    @Column(name = "is_producer")
    private boolean isProducer;

    @Column(name = "is_consumer")
    private boolean isConsumer;

    public Long getTlgChatId() {
        return tlgChatId;
    }

    public void setTlgChatId(Long tlgChatId) {
        this.tlgChatId = tlgChatId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isChannel() {
        return channel;
    }

    public void setChannel(boolean channel) {
        this.channel = channel;
    }

    public Boolean getProducer() {
        return isProducer;
    }

    public void setProducer(Boolean producer) {
        isProducer = producer;
    }

    public Boolean getConsumer() {
        return isConsumer;
    }

    public void setConsumer(Boolean consumer) {
        isConsumer = consumer;
    }
}
