package com.lihuel.discordbot.bots.invisiblefriend.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.bots.invisiblefriend.service.InvisibleFriendGameService;
import com.lihuel.discordbot.discord.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand({"invisible-friend"})

public class InvisibleFriendResetGifts implements Command {
    private final InvisibleFriendGameService invisibleFriendGameService;

    public InvisibleFriendResetGifts(InvisibleFriendGameService invisibleFriendGameService) {
        this.invisibleFriendGameService = invisibleFriendGameService;
    }

    @Override
    public String getName() {
        return "amigo-invisible-reset-gifts";
    }

    @Override
    public String getDescription() {
        return "Reinicia los regalos borrando la base de datos";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        invisibleFriendGameService.resetGifts(event.getGuild().getId());
        event.reply("Regalos reiniciados").queue();
    }
}
