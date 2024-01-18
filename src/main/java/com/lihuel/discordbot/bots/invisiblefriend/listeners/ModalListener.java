package com.lihuel.discordbot.bots.invisiblefriend.listeners;

import com.lihuel.discordbot.bots.invisiblefriend.embeds.InvisibleFriendEmbed;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendGift;
import com.lihuel.discordbot.bots.invisiblefriend.exception.InvisibleFriendGameNotFoundException;
import com.lihuel.discordbot.bots.invisiblefriend.service.InvisibleFriendGameService;
import com.lihuel.discordbot.discord.JDAFactory;
import com.lihuel.discordbot.discord.utils.embeds.AlertEmbed;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModalListener extends ListenerAdapter {
    private final InvisibleFriendGameService invisibleFriendGameService;

    @Autowired
    public ModalListener(InvisibleFriendGameService invisibleFriendGameService, JDAFactory jdaFactory) {
        this.invisibleFriendGameService = invisibleFriendGameService;
        jdaFactory.getJda("invisible-friend").addEventListener(this);
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("sendGift")) {
            String title = event.getValue("title").getAsString();
            String body = event.getValue("body").getAsString();
            String imgURL = event.getValue("imgURL").getAsString();
            InvisibleFriendGift gift = new InvisibleFriendGift();
            gift.setTitle(title);
            gift.setBody(body);
            gift.setImgURL(imgURL);
            event.deferReply().setEphemeral(true).queue();
            try {
                invisibleFriendGameService.sendGift(event.getGuild().getId(), event.getUser().getId(), gift);
            } catch (InvisibleFriendGameNotFoundException e) {
                event.getHook().sendMessageEmbeds(AlertEmbed.createError(e.getMessage())).queue();
                return;
            }
            try {
                event.getHook().sendMessageEmbeds(AlertEmbed.createSuccess("Tu regalo ha sido enviado correctamente. A continuación te mostramos cómo se verá. Puedes modificarlo volviendolo a enviar"), InvisibleFriendEmbed.giftEmbed(title, body, imgURL)).queue();
            }
            catch (Exception e) {
                event.getHook().sendMessageEmbeds(AlertEmbed.createError("Ha ocurrido un error al enviar el regalo. Por favor, inténtalo de nuevo"), AlertEmbed.createError("Es probable que la imagen que pusiste no sea válida. Dejá el campo vacío o poné una url a una imagen válida")).queue();
            }
        }
    }
}