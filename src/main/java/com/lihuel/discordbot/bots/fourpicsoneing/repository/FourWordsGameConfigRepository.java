package com.lihuel.discordbot.bots.fourpicsoneing.repository;

import com.lihuel.discordbot.bots.fourpicsoneing.entity.FourWordsGameConfig;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FourWordsGameConfigRepository extends MongoRepository<FourWordsGameConfig, String> {
}
