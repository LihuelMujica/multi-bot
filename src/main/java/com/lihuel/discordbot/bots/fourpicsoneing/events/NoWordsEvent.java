package com.lihuel.discordbot.bots.fourpicsoneing.events;

import com.lihuel.discordbot.bots.fourpicsoneing.entity.FourWordsGameConfig;
import org.springframework.context.ApplicationEvent;

public class NoWordsEvent extends ApplicationEvent {
    public NoWordsEvent(FourWordsGameConfig config) {
        super(config);
    }

    public FourWordsGameConfig getConfig() {
        return (FourWordsGameConfig) getSource();
    }
}
