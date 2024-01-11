package com.lihuel.discordbot.bots.invisiblefriend.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.bots.invisiblefriend.service.InvisibleFriendAttachService;
import com.lihuel.discordbot.discord.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SlashCommand({"invisible-friend"})
public class InvisibleFriendAddAttach implements Command {
    private final InvisibleFriendAttachService invisibleFriendAttachService;

    @Autowired
    public InvisibleFriendAddAttach(InvisibleFriendAttachService invisibleFriendAttachService) {
        this.invisibleFriendAttachService = invisibleFriendAttachService;
    }

    @Override
    public String getName() {
        return "enviar-regalo-privado";
    }

    @Override
    public String getDescription() {
            return "Envía un regalo para tu amigo, soleo él podrá verlo. Antes debes enviar un regalo público";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.STRING, "regalo", "Regalo privado", true)
                        .setRequired(true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        String attach = event.getOption("regalo").getAsString();
        invisibleFriendAttachService.attach(event.getGuild().getId(), event.getUser().getId(), attach);
        event.reply("Regalo privado enviado").setEphemeral(true).queue();
    }
}
