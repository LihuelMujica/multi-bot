package com.lihuel.discordbot.bots.invisiblefriend.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.bots.invisiblefriend.embeds.InvisibleFriendEmbed;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendGame;
import com.lihuel.discordbot.bots.invisiblefriend.service.InvisibleFriendGameService;
import com.lihuel.discordbot.discord.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

@SlashCommand({"invisible-friend"})
public class InvisibleFriendCreateGroup implements Command {
    private final InvisibleFriendGameService invisibleFriendGameService;

    public InvisibleFriendCreateGroup(InvisibleFriendGameService invisibleFriendGameService) {
        this.invisibleFriendGameService = invisibleFriendGameService;
    }

    @Override
    public String getName() {
        return "amigo-invisible-crear-grupo";
    }

    @Override
    public String getDescription() {
        return "Añade un grupo de jugadores a la partida de amigo invisible";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.USER, "jugador1", "Primer jugador", true),
                new OptionData(OptionType.USER, "jugador2", "Segundo jugador", true),
                new OptionData(OptionType.USER, "jugador3", "Tercer jugador", false),
                new OptionData(OptionType.USER, "jugador4", "Cuarto jugador", false),
                new OptionData(OptionType.USER, "jugador5", "Quinto jugador", false),
                new OptionData(OptionType.USER, "jugador6", "Sexto jugador", false),
                new OptionData(OptionType.USER, "jugador7", "Séptimo jugador", false),
                new OptionData(OptionType.USER, "jugador8", "Octavo jugador", false),
                new OptionData(OptionType.USER, "jugador9", "Noveno jugador", false),
                new OptionData(OptionType.USER, "jugador10", "Décimo jugador", false),
                new OptionData(OptionType.USER, "jugador11", "Undécimo jugador", false),
                new OptionData(OptionType.USER, "jugador12", "Duodécimo jugador", false),
                new OptionData(OptionType.USER, "jugador13", "Decimotercer jugador", false),
                new OptionData(OptionType.USER, "jugador14", "Decimocuarto jugador", false),
                new OptionData(OptionType.USER, "jugador15", "Decimoquinto jugador", false),
                new OptionData(OptionType.USER, "jugador16", "Decimosexto jugador", false),
                new OptionData(OptionType.USER, "jugador17", "Decimoséptimo jugador", false),
                new OptionData(OptionType.USER, "jugador18", "Decimoctavo jugador", false),
                new OptionData(OptionType.USER, "jugador19", "Decimonoveno jugador", false),
                new OptionData(OptionType.USER, "jugador20", "Vigésimo jugador", false),
                new OptionData(OptionType.USER, "jugador21", "Vigésimo primer jugador", false),
                new OptionData(OptionType.USER, "jugador22", "Vigésimo segundo jugador", false),
                new OptionData(OptionType.USER, "jugador23", "Vigésimo tercer jugador", false),
                new OptionData(OptionType.USER, "jugador24", "Vigésimo cuarto jugador", false),
                new OptionData(OptionType.USER, "jugador25", "Vigésimo quinto jugador", false)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        List<String> players = event.getOptions().stream()
                .map(option -> option.getAsUser().getId())
                .toList();
        InvisibleFriendGame game = invisibleFriendGameService.addGroup(event.getGuild().getId(), players);
        event.replyEmbeds(InvisibleFriendEmbed.GameInfoEmbed(game)).queue();
    }
}
