package com.lihuel.discordbot.bots.fourpicsoneing.listeners;

import com.lihuel.discordbot.bots.fourpicsoneing.entity.Pics;
import com.lihuel.discordbot.bots.fourpicsoneing.entity.WordSuggestion;
import com.lihuel.discordbot.bots.fourpicsoneing.service.WordService;
import com.lihuel.discordbot.discord.JDAFactory;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ModalFWOI extends ListenerAdapter {

    private final WordService wordService;

    public ModalFWOI(WordService wordService, JDAFactory jdaFactory) {
        this.wordService = wordService;
        jdaFactory.getJda("4fotos-1ing").addEventListener(this);
        System.out.println("Registrado");
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        System.out.println(event.getModalId());
        if (event.getModalId().equals("suggestion")) processSuggestion(event);
    }

    @Async
    public void processSuggestion(ModalInteractionEvent event) {
        event
                .reply("Gracias por tu sugerencia")
                .setEphemeral(true)
                .queue();
        String word = event.getValue("word-suggested").getAsString();
        String img1 = event.getValue("img1").getAsString();
        String img2 = event.getValue("img2").getAsString();
        String img3 = event.getValue("img3").getAsString();
        String img4 = event.getValue("img4").getAsString();
        String authorId = event.getUser().getId();
        String authorName = event.getUser().getName();
        String guildId = event.getGuild().getId();
        WordSuggestion wordSuggestion = new WordSuggestion();
        wordSuggestion.setWord(word);
        wordSuggestion.setAuthorId(authorId);
        wordSuggestion.setAuthorName(authorName);
        wordSuggestion.setGuildId(guildId);
        wordSuggestion.setPics(new Pics(img1, img2, img3, img4, null));
        wordService.sendSuggestion(wordSuggestion);
    }
}
