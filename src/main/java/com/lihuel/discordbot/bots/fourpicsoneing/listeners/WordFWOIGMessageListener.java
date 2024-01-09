package com.lihuel.discordbot.bots.fourpicsoneing.listeners;

import com.lihuel.discordbot.bots.fourpicsoneing.service.WordService;
import com.lihuel.discordbot.discord.JDAFactory;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WordFWOIGMessageListener extends ListenerAdapter {
    private final WordService wordService;

    @Autowired
    public WordFWOIGMessageListener(WordService wordService, JDAFactory jdaFactory) {
        this.wordService = wordService;
        jdaFactory.getJda("4fotos-1ing").addEventListener(this);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String guildId = event.getGuild().getId();
        String channelId = event.getChannel().getId();
        if(event.getAuthor().isBot()) return;
        if(!event.getChannelType().isGuild()) return;
        String guildGameChannelId = wordService.getGameChannel(guildId);
        if(!channelId.equals(guildGameChannelId)) return;
        String answer = event.getMessage().getContentRaw();
        wordService.sendAnswer(event.getGuild().getId(), event.getMessage(), answer);
    }


}
