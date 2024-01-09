package com.lihuel.discordbot.bots.fourpicsoneing.notifications;

import com.lihuel.discordbot.bots.fourpicsoneing.events.LeaderChangeEvent;
import com.lihuel.discordbot.discord.JDAFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class LeaderChangeNotification {
    private final JDA jda;

    public LeaderChangeNotification(JDAFactory jdaFactory) {
        this.jda = jdaFactory.getJda("4fotos-1ing");
    }

    @EventListener
    @Async
    public void onLeaderChange(LeaderChangeEvent event) {
        Guild guild = jda.getGuildById(event.getGuildId());
        Role ingRole = guild.getRolesByName("El ingeniero", true).get(0);
        if (event.getOldLeaderId() != null && !event.getOldLeaderId().isEmpty() ) {
            Member oldLeader = guild.retrieveMemberById(event.getOldLeaderId()).complete();
            guild.removeRoleFromMember(Objects.requireNonNull(oldLeader), ingRole).queue();
        }
        Member newLeader = guild.retrieveMemberById(event.getNewLeaderId()).complete();
        guild.addRoleToMember(Objects.requireNonNull(newLeader), ingRole).queue();
    }

}
