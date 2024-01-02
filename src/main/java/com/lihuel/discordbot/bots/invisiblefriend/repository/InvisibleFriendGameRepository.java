package com.lihuel.discordbot.bots.invisiblefriend.repository;


import com.lihuel.discordbot.bots.invisiblefriend.entity.GameStatus;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendGame;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvisibleFriendGameRepository extends MongoRepository<InvisibleFriendGame, String> {
    Optional<InvisibleFriendGame> findByGuildIdAndStatus(String guildId, GameStatus gameStatus);

    @Query(value = "{ 'guildId' : ?0, 'status' : { $in: [ 'CREATED', 'STARTED' ] }, 'groups' : { $elemMatch: { 'users' : { $elemMatch: { 'userId' : ?1 } } } } }", exists = true)
    boolean playerIsInGame(String guildId, String userId);
}
