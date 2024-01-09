package com.lihuel.discordbot.bots.fourpicsoneing.repository;

import com.lihuel.discordbot.bots.fourpicsoneing.entity.Word;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends MongoRepository<Word, String> {
    @Aggregation(pipeline = {
            "{$match: {playedGuilds: {$nin: [?0]}}}",
            "{$sample: {size: 1}}"
    })
    Word getRandomWord(String guildId);

    @Aggregation(pipeline = {
            "{$match: {playedGuilds: {$in: [?0]}}}"
    })
    List<Word> getAllWordsSended(String guildId);
}
