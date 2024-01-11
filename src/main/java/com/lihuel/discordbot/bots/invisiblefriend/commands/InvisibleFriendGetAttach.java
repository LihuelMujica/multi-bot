package com.lihuel.discordbot.bots.invisiblefriend.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendPrivateAttach;
import com.lihuel.discordbot.bots.invisiblefriend.service.InvisibleFriendAttachService;
import com.lihuel.discordbot.discord.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SlashCommand({"invisible-friend"})
public class InvisibleFriendGetAttach implements Command {
    private final InvisibleFriendAttachService invisibleFriendAttachService;

    @Autowired
    public InvisibleFriendGetAttach(InvisibleFriendAttachService invisibleFriendAttachService) {
        this.invisibleFriendAttachService = invisibleFriendAttachService;
    }

    @Override
    public String getName() {
        return "abrir-paquete";
    }

    @Override
    public String getDescription() {
        return "Si tu amigo invisible te mandó un regalo privado, con este comando puedes verlo";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        List< InvisibleFriendPrivateAttach > invisibleFriendPrivateAttachList = invisibleFriendAttachService.getAttach(event.getGuild().getId(), event.getUser().getId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Regalos privados:\n");
        invisibleFriendPrivateAttachList.forEach(invisibleFriendPrivateAttach -> stringBuilder.append(invisibleFriendPrivateAttach.getAttach()).append("\n"));
        event.reply(stringBuilder.toString()).setEphemeral(true).queue();
    }
}
