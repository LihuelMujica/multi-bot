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
public class StartFWOIGame implements Command {
    private final WordService wordService;

    @Autowired
    public StartFWOIGame(WordService wordService) {
        this.wordService = wordService;
    }

    @Override
    public String getName() {
        return "iniciar";
    }

    @Override
    public String getDescription() {
        return "Inicia el juego en el canal actual";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Async
    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        FourWordsGameConfig gameConfig = wordService.startGame(event.getGuild().getId(), event.getChannel().getId());
        List<Role> elIng = event.getGuild().getRolesByName("El Ingeniero", true);
        if (elIng.isEmpty()) {
            event.getGuild().createRole().setName("El Ingeniero").queue();
        }
        if (gameConfig.getCurrentWord() == null) {
            event.replyEmbeds(AlertEmbed.createError("No se encontró ninguna palabra")).queue();
            return;
        }
        event.replyEmbeds(AlertEmbed.createSuccess("Se ha iniciado el juego")).queue();

    }
}
