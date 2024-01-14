package com.lihuel.discordbot.bots.invisiblefriend.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.bots.invisiblefriend.embeds.InvisibleFriendEmbed;
import com.lihuel.discordbot.bots.invisiblefriend.service.InvisibleFriendGameService;
import com.lihuel.discordbot.discord.commands.Command;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand({"invisible-friend"})

public class InvisibleFriendShowGifts implements Command {
    private final InvisibleFriendGameService invisibleFriendGameService;

    public InvisibleFriendShowGifts(InvisibleFriendGameService invisibleFriendGameService) {
        this.invisibleFriendGameService = invisibleFriendGameService;
    }

    @Override
    public String getName() {
        return "amigo-invisible-mostrar-regalos";
    }

    @Override
    public String getDescription() {
        return "Muestra los regalos de la partida";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        event.reply("Los regalos son:").queue();
        List<MessageEmbed> gifts = invisibleFriendGameService.getGifts(event.getGuild().getId()).stream()
                        .map(gift -> {
                            return InvisibleFriendEmbed.giftEmbed(gift.getTitle(), gift.getBody(), gift.getImgURL(), gift.getReceiverId());
                        }).toList();
        gifts.forEach(gift -> event.getChannel().sendMessageEmbeds(gift).queue());
    }
}
