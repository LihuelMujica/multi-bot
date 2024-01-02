package com.lihuel.discordbot.bots.invisiblefriend.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "invisibleFriendGames")
public class InvisibleFriendGame {
    @Id
    private String gameId;
    @Indexed
    private String guildId;
    private GameStatus status;
    private List<List<InvisibleFriendUser>> groups = new ArrayList<>();
}
