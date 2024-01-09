package com.lihuel.discordbot.bots.fourpicsoneing.notifications;

import com.lihuel.discordbot.bots.fourpicsoneing.entity.Word;
import com.lihuel.discordbot.bots.fourpicsoneing.events.NoWordsEvent;
import com.lihuel.discordbot.bots.fourpicsoneing.events.WordChangeEvent;
import com.lihuel.discordbot.discord.JDAFactory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.*;

@Component
public class WordChangeNotification {
    private final JDAFactory jdaFactory;
    private final ApplicationContext applicationContext;

    @Autowired
    public WordChangeNotification(JDAFactory jdaFactory, ApplicationContext applicationContext) {
        this.jdaFactory = jdaFactory;
        this.applicationContext = applicationContext;
    }

    @EventListener
    public void onWordChange(WordChangeEvent event) {
        if (event.getGameConfig().getCurrentWord() == null ){
            applicationContext.publishEvent(new NoWordsEvent(event.getGameConfig()));
            return;
        }
        JDA jda = jdaFactory.getJda("4fotos-1ing");
        if (event.getMessage() == null) {
            jda.getGuildById(event.getGameConfig().getGuildId()).getTextChannelById(event.getGameConfig().getGameChannelId()).sendMessageEmbeds(getEmbed(event.getGameConfig().getCurrentWord())).
                    addActionRow(Button.primary("sugerencias", "sugerencias"), Button.danger("imagen-error", "la imagen no carga")).queue();
        }
        else if (event.getGameConfig().getCurrentWord() != null){
            event.getMessage().replyEmbeds(getEmbed(event.getGameConfig().getCurrentWord(), event.getMessage().getAuthor().getId(), event.getGameConfig().getCurrentLeader().l)).
                    addActionRow(Button.primary("sugerencias", "sugerencias"), Button.danger("imagen-error", "la imagen no carga")).queue();
        }
        else {
            event.getMessage().replyEmbeds(noWordsEmbed()).addActionRow(Button.primary("sugerencias", "sugerencias")).queue();
        }

    }

    @EventListener
    public void onNoWords(NoWordsEvent event) {
        JDA jda = jdaFactory.getJda("4fotos-1ing");
        jda.getGuildById(event.getConfig().getGuildId()).getTextChannelById(event.getConfig().getGameChannelId()).sendMessageEmbeds(noWordsEmbed()).
                addActionRow(Button.primary("sugerencias", "sugerencias")).queue();
    }

    public MessageEmbed getEmbed(Word word, String userId, String theIng) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder descriptionSb = new StringBuilder();
        descriptionSb.append("**PARA PARTICIPAR ENVIÁ LA PALABRA QUE CREES QUE REPRESENTAN ESTAS 4 IMÁGENES**");
        if (userId != null) {
            descriptionSb.append("\n");
            descriptionSb.append("<@").append(userId).append("> adivinó la última palabra");
        }
        embedBuilder.setDescription("");
        String groupedPic = word.getPics().getGroupedPic();
        System.out.println(groupedPic);
        embedBuilder.setImage(groupedPic);
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setThumbnail("https://imgs.search.brave.com/nvJsTXp2-SWOxlh_8mqT-Wld470QXxOIm0ygdeEu_BI/rs:fit:860:0:0/g:ce/aHR0cHM6Ly9kMW5o/aW8wb3g3cGdiLmNs/b3VkZnJvbnQubmV0/L19pbWcvb19jb2xs/ZWN0aW9uX3BuZy9n/cmVlbl9kYXJrX2dy/ZXkvNTEyeDUxMi9w/bGFpbi9lbmdpbmVl/ci5wbmc");

        StringBuilder elIngSb = new StringBuilder();
        if(theIng != null) {
            elIngSb.append("<@").append(theIng).append("> es el ingeniero");
            elIngSb.append("\n");
            embedBuilder.addField("El ingeniero", elIngSb.toString(), false);

        }
        embedBuilder.setDescription(descriptionSb.toString());
        MessageEmbed embed = embedBuilder.build();
        System.out.println(embed.getImage());
        return embed;
    }

    public MessageEmbed getEmbed(Word word) {
        return getEmbed(word, null, null);
    }

    public MessageEmbed noWordsEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("4 FOTOS 1 INGENIERO");
        embedBuilder.setDescription("**NO HAY MÁS PALABRAS DISPONIBLES**");
        embedBuilder.addField("", "Si querés agregar palabras, hacé click en el botón de sugerencias", false);
        embedBuilder.setColor(Color.ORANGE);
        embedBuilder.setThumbnail("https://imgs.search.brave.com/nvJsTXp2-SWOxlh_8mqT-Wld470QXxOIm0ygdeEu_BI/rs:fit:860:0:0/g:ce/aHR0cHM6Ly9kMW5o/aW8wb3g3cGdiLmNs/b3VkZnJvbnQubmV0/L19pbWcvb19jb2xs/ZWN0aW9uX3BuZy9n/cmVlbl9kYXJrX2dy/ZXkvNTEyeDUxMi9w/bGFpbi9lbmdpbmVl/ci5wbmc");
        return embedBuilder.build();
    }
}
