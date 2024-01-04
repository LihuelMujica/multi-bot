package com.lihuel.discordbot.discord.listeners;

import com.lihuel.discordbot.discord.JDAFactory;
import com.lihuel.discordbot.discord.utils.embeds.AlertEmbed;
import com.lihuel.discordbot.repository.PrivateMessageSenderRepository;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ModalListenerPrivateMessageSender extends ListenerAdapter {
    private final PrivateMessageSenderRepository privateMessageSenderRepository;
    private final JDAFactory jdaFactory;

    @Autowired
    public ModalListenerPrivateMessageSender(JDAFactory jdaFactory, PrivateMessageSenderRepository privateMessageSenderRepository) {
        this.privateMessageSenderRepository = privateMessageSenderRepository;
        this.jdaFactory = jdaFactory;
        jdaFactory.getJda("invocador").addEventListener(this);
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        String modalId = event.getModalId();
        System.out.println(event);
        System.out.println(event.getModalId());
        System.out.println(event.getModalId().startsWith("privateEmbed"));

        if (modalId.startsWith("privateEmbed")) {
            String json = event.getValue("jsonEmbed").getAsString();
            var privateMessage = privateMessageSenderRepository.getMessageDataByModalID(modalId);
            if (privateMessage == null) {
                event.getHook().sendMessageEmbeds(AlertEmbed.createError("No se ha encontrado el mensaje")).setEphemeral(true).queue();
                return;
            }

            List<String> userIds = privateMessage.getUserIds();
            String botId = privateMessage.getBotId();
            JDA botJda = jdaFactory.getJdaById(botId);
            System.out.println(botJda);
            System.out.println(botId);
            String guildId = event.getGuild().getId();

            userIds.forEach( userId -> sendPrivateMessage(botJda, guildId, userId, json, event));
            event.reply("Mensaje enviado").setEphemeral(true).queue();

        }
    }

    private void sendPrivateMessage(JDA jda, String guildId, String userId, String message, ModalInteractionEvent event) {
        try {
            jda.getGuildById(guildId).retrieveMemberById(userId).queue( member -> {
                member.getUser().openPrivateChannel().queue( privateChannel -> {
                    privateChannel.sendMessage(message).queue();
                });
            });
        }
        catch (Exception e) {
            event.getChannel().sendMessage("No se ha podido enviar el mensaje privado a <@" + userId + ">").queue();
        }

    }
}