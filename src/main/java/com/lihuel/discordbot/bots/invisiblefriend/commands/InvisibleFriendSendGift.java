package com.lihuel.discordbot.bots.invisiblefriend.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.bots.invisiblefriend.service.InvisibleFriendGameService;
import com.lihuel.discordbot.discord.commands.Command;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;

@SlashCommand({"invisible-friend"})
public class InvisibleFriendSendGift implements Command {
    private final InvisibleFriendGameService invisibleFriendGameService;

    public InvisibleFriendSendGift(InvisibleFriendGameService invisibleFriendGameService) {
        this.invisibleFriendGameService = invisibleFriendGameService;
    }

    @Override
    public String getName() {
        return "amigo-invisible-enviar-regalo";
    }

    @Override
    public String getDescription() {
        return "Envía un regalo a tu amigo";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        TextInput title = TextInput.create("title", "titulo", TextInputStyle.SHORT)
                .setPlaceholder("Escribe el título del regalo")
                .setRequired(false)
                .setMaxLength(50)
                .build();
        TextInput body = TextInput.create("body", "cuerpo", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Escribe el cuerpo del regalo")
                .setRequired(true)
                .setMaxLength(1500)
                .build();
        TextInput imgURL = TextInput.create("imgURL", "imagen", TextInputStyle.SHORT)
                .setPlaceholder("Escribe la URL de la imagen del regalo")
                .setRequired(false)
                .build();
        Modal modal = Modal.create("sendGift", "Enviar regalo")
                .setTitle("Envía un regalo a tu amigo")
                .addComponents(ActionRow.of(title), ActionRow.of(body), ActionRow.of(imgURL))
                .build();
        event.replyModal(modal).queue();
    }
}
