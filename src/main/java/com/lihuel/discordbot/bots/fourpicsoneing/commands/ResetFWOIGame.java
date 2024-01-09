package com.lihuel.discordbot.bots.fourpicsoneing.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.bots.fourpicsoneing.entity.FourWordsGameConfig;
import com.lihuel.discordbot.bots.fourpicsoneing.service.WordService;
import com.lihuel.discordbot.discord.commands.Command;
import com.lihuel.discordbot.discord.utils.embeds.AlertEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

@SlashCommand({"4fotos-1ing"})
public class ResetFWOIGame implements Command {
    private final WordService wordService;

    @Autowired
    public ResetFWOIGame(WordService wordService) {
        this.wordService = wordService;
    }

    @Override
    public String getName() {
        return "resetear-palabras";
    }

    @Override
    public String getDescription() {
        return "Reinicia las palabras del juego de 4 fotos 1 ingeniero";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Async
    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        wordService.resetServerWords(event.getGuild().getId());
        event.replyEmbeds(AlertEmbed.createSuccess("Se han reiniciado las palabras del juego")).queue();

    }
}
