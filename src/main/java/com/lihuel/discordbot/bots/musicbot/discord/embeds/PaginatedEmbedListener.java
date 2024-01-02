package com.lihuel.discordbot.bots.musicbot.discord.embeds;

import com.lihuel.discordbot.discord.JDAFactory;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaginatedEmbedListener extends ListenerAdapter {
    private final EmbedRepository embedRepository;

    @Autowired
    public PaginatedEmbedListener(EmbedRepository embedRepository, JDAFactory jdaFactory) {
        this.embedRepository = embedRepository;
        jdaFactory.getJda("c-3po").addEventListener(this);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getButton().getId();
        String messageId = buttonId.substring(buttonId.indexOf(":") + 1);
        PaginatedEmbed embed = embedRepository.getPaginatedEmbed(messageId);
        if (embed == null) {
            Message message = event.getMessage();
            message.editMessageEmbeds(message.getEmbeds().get(0)).setComponents().queue();
            return;
        }
        if (buttonId.startsWith("next")) {
            embed.next();
        } else if (buttonId.startsWith("prev")) {
            embed.previous();
        }
        event.deferEdit().queue();
    }

}
