package ru.airiva.vo;

import java.util.Objects;

/**
 * @author Ivan
 */
public class TlgChannel implements Comparable<TlgChannel> {

    public final int id;
    private String title;
    private long chatId;
    private String username;
    private Boolean isConsumer;
    private Boolean isProducer;
    boolean admin = false;

    public void setTitle(String title) {
        this.title = title != null ? title : "unknown";
    }

    public String getTitle() {
        return title;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public TlgChannel(int id) {
        this.id = id;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public int getId() {
        return id;
    }

    public Boolean getConsumer() {
        return isConsumer;
    }

    public void setConsumer(Boolean consumer) {
        isConsumer = consumer;
    }

    public Boolean getProducer() {
        return isProducer;
    }

    public void setProducer(Boolean producer) {
        isProducer = producer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TlgChannel that = (TlgChannel) o;
        return id == that.id &&
                chatId == that.chatId &&
                Objects.equals(title, that.title);
    }

    @Override
    public int compareTo(TlgChannel o) {
        if (!this.title.equals(o.title)) {
            return this.title.compareTo(o.title);
        }
        if (this.id != o.id) {
            return this.id < o.id ? -1 : 1;
        }
        return 0;
    }

}
