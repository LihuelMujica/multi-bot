package com.lihuel.discordbot.bots.invisiblefriend.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.bots.invisiblefriend.service.InvisibleFriendGameService;
import com.lihuel.discordbot.discord.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.stereotype.Component;

import java.util.List;

@SlashCommand({"invisible-friend"})
public class InvisibleFriendCreateGame implements Command {
    private final InvisibleFriendGameService invisibleFriendGameService;

    public InvisibleFriendCreateGame(InvisibleFriendGameService invisibleFriendGameService) {
        this.invisibleFriendGameService = invisibleFriendGameService;
    }

    @Override
    public String getName() {
        return "amigo-invisible-crear";
    }

    @Override
    public String getDescription() {
        return "Crea una partida de amigo invisible";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        invisibleFriendGameService.createGame(event.getGuild().getId());
        event.reply("Partida de amigo invisible creada").queue();
    }
}
