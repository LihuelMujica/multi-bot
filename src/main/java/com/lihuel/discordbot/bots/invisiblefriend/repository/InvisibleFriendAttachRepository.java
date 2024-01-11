package com.lihuel.discordbot.bots.invisiblefriend.repository;

import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendPrivateAttach;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InvisibleFriendAttachRepository extends MongoRepository<InvisibleFriendPrivateAttach, String> {
    List<InvisibleFriendPrivateAttach> findByGameIdAndReceiverId(String gameId, String receiverId);
}
