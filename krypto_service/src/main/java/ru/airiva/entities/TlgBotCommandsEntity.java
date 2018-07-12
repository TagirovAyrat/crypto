package ru.airiva.entities;

import javax.persistence.*;

import static ru.airiva.entities.EntityConstants.*;

@Entity(name = TLG_BOT_COMMANDS)
@SequenceGenerator(name = TLG_BOT_COMMANDS_GEN, sequenceName = TLG_BOT_COMMANDS_SEQ, allocationSize = 1)
public class TlgBotCommandsEntity {

    @Id
    @GeneratedValue(generator = TLG_BOT_TRANSLATIONS_GEN, strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "command_name")
    private String commandName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
}

