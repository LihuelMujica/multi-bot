package com.lihuel.discordbot.bots.invisiblefriend.entity;

import lombok.Data;

@Data
public class InvisibleFriendUser {
    private String userId;
    private String friendId;
    private InvisibleFriendGift giftSent;
}
