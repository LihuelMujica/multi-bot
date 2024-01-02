package com.lihuel.discordbot.bots.invisiblefriend.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.bots.invisiblefriend.embeds.InvisibleFriendEmbed;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendGame;
import com.lihuel.discordbot.bots.invisiblefriend.service.InvisibleFriendGameService;
import com.lihuel.discordbot.discord.commands.Command;
import com.lihuel.discordbot.discord.utils.embeds.AlertEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand({"invisible-friend"})
public class InvisibleFriendRemoveGroup implements Command {
    private final InvisibleFriendGameService invisibleFriendGameService;

    public InvisibleFriendRemoveGroup(InvisibleFriendGameService invisibleFriendGameService) {
        this.invisibleFriendGameService = invisibleFriendGameService;
    }

    @Override
    public String getName() {
        return "amigo-invisible-eliminar-grupo";
    }

    @Override
    public String getDescription() {
        return "Elimina un grupo de jugadores";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.INTEGER, "id", "ID del grupo a eliminar", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        int groupId = event.getOptions().get(0).getAsInt();
        InvisibleFriendGame game = invisibleFriendGameService.removeGroup(event.getGuild().getId(), groupId);
        event.replyEmbeds(AlertEmbed.createSuccess("Se ha eliminado el grupo correctamente"), InvisibleFriendEmbed.GameInfoEmbed(game)).queue();
    }
}
