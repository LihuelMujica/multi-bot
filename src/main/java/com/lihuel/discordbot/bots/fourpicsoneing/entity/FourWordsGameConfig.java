package com.lihuel.discordbot.bots.fourpicsoneing.entity;

import com.lihuel.discordbot.bots.fourpicsoneing.events.LeaderChangeEvent;
import com.sedmelluq.lava.extensions.youtuberotator.tools.Tuple;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContext;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "4fotos1IngenieroGuilds")
@Data
public class FourWordsGameConfig {
    @Id
    private String guildId;
    private String gameChannelId;
    private Word currentWord;
    private String currentWordId;
    /**
     * Map of users that have already answered
     * each correct answer is worth 1 point
     *
     * @key user id
     * @value score
     */
    private Map<String, Integer> scores = new HashMap<>();
    Tuple<String, Integer> currentLeader = new Tuple<>("", 0);

    public void addScore(@NotNull String userId, ApplicationContext applicationContext) {
        if (scores.containsKey(userId)) {
            scores.put(userId, scores.get(userId) + 1);
        } else {
            scores.put(userId, 1);
        }
        if (currentLeader.r < scores.get(userId) || currentLeader.l.isEmpty()) {
            String oldLeaderId = currentLeader.l;
            currentLeader = new Tuple<>(userId, scores.get(userId));
            applicationContext.publishEvent(new LeaderChangeEvent(oldLeaderId, userId, guildId));
        }
    }
}
