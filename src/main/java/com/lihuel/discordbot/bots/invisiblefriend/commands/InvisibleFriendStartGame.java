package com.lihuel.discordbot.bots.invisiblefriend.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendGame;
import com.lihuel.discordbot.bots.invisiblefriend.service.InvisibleFriendGameService;
import com.lihuel.discordbot.discord.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand({"invisible-friend"})
public class InvisibleFriendStartGame implements Command {
    private final InvisibleFriendGameService invisibleFriendGameService;

    public InvisibleFriendStartGame(InvisibleFriendGameService invisibleFriendGameService) {
        this.invisibleFriendGameService = invisibleFriendGameService;
    }

    @Override
    public String getName() {
        return "amigo-invisible-iniciar";
    }

    @Override
    public String getDescription() {
        return "Inicia una partida de amigo invisible";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        InvisibleFriendGame game = invisibleFriendGameService.startGame(event.getGuild().getId());
        event.reply("Partida de amigo invisible iniciada").queue();
        game.getGroups().forEach( g -> g.forEach( u -> sendPrivateMessage(event, u.getUserId(), u.getFriendId())));
    }

    private void sendPrivateMessage(SlashCommandInteractionEvent event, String userId, String friendId) {
        try {
            event.getGuild().retrieveMemberById(userId).queue( member -> {
                member.getUser().openPrivateChannel().queue( privateChannel -> {
                    privateChannel.sendMessage("Eres el amigo invisible de <@" + friendId + ">").queue();
                });
            });
        }
        catch (Exception e) {
            event.getChannel().sendMessage("No se ha podido enviar el mensaje privado a <@" + userId + ">").queue();
        }

    }
}
