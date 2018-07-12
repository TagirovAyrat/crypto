package ru.airiva.entities;

import javax.persistence.*;

import static ru.airiva.entities.EntityConstants.*;

/**
 * @author Ivan
 */
@Entity
@Table(name = TLG_BOT_TRANSLATIONS)
@SequenceGenerator(name = TLG_BOT_TRANSLATIONS_GEN, sequenceName = TLG_BOT_TRANSLATIONS_SEQ, allocationSize = 1)
public class TlgBotTranslationEntity {

    @Id
    @GeneratedValue(generator = TLG_BOT_TRANSLATIONS_GEN, strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(name="locale")
    private String locale;
    @Column(name="command")
    private String command;
    @Column(name = "translated_command")
    private String translatedCommand;
    @Column(name = "command_text")
    private String commandText;
    @Column(name = "start_emoji")
    private String emojiAtBegin;
    @Column(name = "end_emoji")
    private String emojiAtEnd;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommandText() {
        return commandText;
    }

    public void setCommandText(String commandText) {
        this.commandText = commandText;
    }

    public String getEmojiAtBegin() {
        return emojiAtBegin;
    }

    public void setEmojiAtBegin(String emojiAtBegin) {
        this.emojiAtBegin = emojiAtBegin;
    }

    public String getEmojiAtEnd() {
        return emojiAtEnd;
    }

    public void setEmojiAtEnd(String emojiAtEnd) {
        this.emojiAtEnd = emojiAtEnd;
    }

    public String getTranslatedCommand() {
        return translatedCommand;
    }

    public void setTranslatedCommand(String translatedCommand) {
        this.translatedCommand = translatedCommand;
    }
}
