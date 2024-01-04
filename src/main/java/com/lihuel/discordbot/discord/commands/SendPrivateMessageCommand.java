package com.lihuel.discordbot.discord.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.discord.JDAFactory;
import com.lihuel.discordbot.repository.PrivateMessageSenderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.List;
import java.util.UUID;

@SlashCommand({"invocador"})
public class SendPrivateMessageCommand implements Command {
    private final JDAFactory jdaFactory;
    private final PrivateMessageSenderRepository messagesRepository;

    public SendPrivateMessageCommand(JDAFactory jdaFactory, PrivateMessageSenderRepository messagesRepository) {
        this.jdaFactory = jdaFactory;
        this.messagesRepository = messagesRepository;
    }

    @Override
    public String getName() {
        return "enviar-mensaje-privado";
    }

    @Override
    public String getDescription() {
        return "Envía un mensaje privado a una lista de usuarios";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new java.util.ArrayList<>(List.of(
                new OptionData(OptionType.USER, "bot", "Bot que enviará el mensaje", true)
        ));
        for (int i = 1; i < 20; i++) {
            options.add(new OptionData(OptionType.USER, "usuario" + i, "Usuario al que enviar el mensaje", false));
        }
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        String botId = event.getOption("bot").getAsString();
        List<String> userIds = event.getOptions().subList(1, event.getOptions().size()).stream().map(OptionMapping::getAsString).toList();
        JDA bot = jdaFactory.getJdaById(botId);
        if (bot == null) {
            event.reply("El bot no está disponible o no existe").setEphemeral(true).queue();
            return;
        }
        TextInput jsonEmbed = TextInput.create("jsonEmbed", "JSON del embed", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Escribe el mensaje que enviará el bot")
                .setRequired(true)
                .build();
        Modal modal = Modal.create("privateEmbed:" + UUID.randomUUID(), "Enviar embed")
                .addComponents(ActionRow.of(jsonEmbed))
                .build();
        messagesRepository.createMessageModal(modal.getId(), userIds, botId);
        event.replyModal(modal).queue();
    }

    @AllArgsConstructor
    @Data
    public static class PrivateMessageModal {
        private List<String> userIds;
        private String botId;
    }


}
