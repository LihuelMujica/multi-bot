package com.lihuel.discordbot.bots.fourpicsoneing.interactions;

import com.lihuel.discordbot.bots.fourpicsoneing.entity.FourWordsGameConfig;
import com.lihuel.discordbot.bots.fourpicsoneing.service.WordService;
import com.lihuel.discordbot.discord.JDAFactory;
import com.lihuel.discordbot.discord.utils.embeds.AlertEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ButtonInteractions extends ListenerAdapter {
    private final WordService wordService;

    @Autowired
    public ButtonInteractions(WordService wordService, JDAFactory jdaFactory) {
        this.wordService = wordService;
        jdaFactory.getJda("4fotos-1ing").addEventListener(this);
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        switch (buttonId) {
            case "imagen-error":
                reloadImage(event);
                break;
            case "sugerencias":
                sendSuggestion(event);
                break;
        }
    }

    public void reloadImage(ButtonInteractionEvent event) {
        FourWordsGameConfig config = wordService.getGuildImage(event.getGuild().getId());
        if (config.getCurrentWord() == null) {
            event.replyEmbeds(AlertEmbed.createError("No hay ninguna palabra cargada"))
                    .setEphemeral(true)
                    .queue();
            return;
        }
        event.reply(config.getCurrentWord().getPics().getGroupedPic())
                .setEphemeral(true)
                .queue();
    }

    public void sendSuggestion(ButtonInteractionEvent event) {
        TextInput textInput = TextInput.create("word-suggested", "Palabra", TextInputStyle.SHORT)
                        .setMaxLength(15).build();
        TextInput img1 = TextInput.create("img1", "imagen 1", TextInputStyle.SHORT).build();
        TextInput img2 = TextInput.create("img2", "imagen 2", TextInputStyle.SHORT).build();
        TextInput img3 = TextInput.create("img3", "imagen 3", TextInputStyle.SHORT).build();
        TextInput img4 = TextInput.create("img4", "imagen 4", TextInputStyle.SHORT).build();
        Modal modal = Modal.create("suggestion", "Sugiere una palabra")
                        .addComponents(ActionRow.of(textInput),
                                ActionRow.of(img1),
                                ActionRow.of(img2),
                                ActionRow.of(img3),
                                ActionRow.of(img4))
                        .build();
        event.replyModal(modal).queue();
    }
}
