package com.lihuel.discordbot.bots.fourpicsoneing.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "4fotos1Ingeniero")
public class Word {
    @Id
    private String id;
    private String word;
    private Pics pics;
    private String authorId;
    private String authorName;
    private List<String> playedGuilds;

    public List<String> getPlayedGuilds() {
        if (playedGuilds == null) {
            playedGuilds = new ArrayList<>();
        }
        return playedGuilds;
    }
}
