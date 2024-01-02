package com.lihuel.discordbot.bots.invisiblefriend.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.bots.invisiblefriend.embeds.InvisibleFriendEmbed;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendGame;
import com.lihuel.discordbot.bots.invisiblefriend.exception.InvisibleFriendGameNotFoundException;
import com.lihuel.discordbot.bots.invisiblefriend.service.InvisibleFriendGameService;
import com.lihuel.discordbot.discord.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand({"invisible-friend"})
public class InvisibleFriendGameInfo implements Command {
    private final InvisibleFriendGameService invisibleFriendGameService;

    public InvisibleFriendGameInfo(InvisibleFriendGameService invisibleFriendGameService) {
        this.invisibleFriendGameService = invisibleFriendGameService;
    }

    @Override
    public String getName() {
        return "amigo-invisible-info";
    }

    @Override
    public String getDescription() {
        return "Información sobre la partida de amigo invisible";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws InvisibleFriendGameNotFoundException {
        InvisibleFriendGame game = invisibleFriendGameService.getGame(event.getGuild().getId());
        event.replyEmbeds(InvisibleFriendEmbed.GameInfoEmbed(game)).queue();

    }
}
