package com.lihuel.discordbot.bots.fourpicsoneing.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "4fotos1IngenieroWordSuggestion")
@Data
public class WordSuggestion {
    @Id
    private String id;
    private String word;
    private String authorId;
    private String authorName;
    private String guildId;
    private Pics pics;
}
