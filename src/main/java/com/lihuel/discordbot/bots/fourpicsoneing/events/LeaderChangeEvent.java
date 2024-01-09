package com.lihuel.discordbot.bots.fourpicsoneing.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class LeaderChangeEvent extends ApplicationEvent {
    private final String oldLeaderId;
    private final String newLeaderId;
    private final String guildId;
    public LeaderChangeEvent(String oldLeaderId, String newLeaderId, String guildId) {
        super(guildId);
        this.oldLeaderId = oldLeaderId;
        this.newLeaderId = newLeaderId;
        this.guildId = guildId;
    }
}
