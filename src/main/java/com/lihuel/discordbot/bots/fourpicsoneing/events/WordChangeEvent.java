package com.lihuel.discordbot.bots.fourpicsoneing.events;

import com.lihuel.discordbot.bots.fourpicsoneing.entity.FourWordsGameConfig;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.context.ApplicationEvent;

import java.util.List;
import java.util.Objects;

public class WordChangeEvent extends ApplicationEvent {

    private final Message message;
    public WordChangeEvent(FourWordsGameConfig gameConfig, Message message) {
        super(gameConfig);
        this.message = message;
    }

    public FourWordsGameConfig getGameConfig() {
        return (FourWordsGameConfig) getSource();
    }
    public Message getMessage() {
        return message;
    }
}
