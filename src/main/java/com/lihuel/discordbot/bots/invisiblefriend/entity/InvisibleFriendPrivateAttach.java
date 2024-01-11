package com.lihuel.discordbot.bots.invisiblefriend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "invisible-friend-private-attach")
@Data
@NoArgsConstructor
public class InvisibleFriendPrivateAttach {
    @Id
    private String attachId;

    @Indexed
    private String gameId;

    @Indexed
    private String receiverId;

    private String attach;
}
