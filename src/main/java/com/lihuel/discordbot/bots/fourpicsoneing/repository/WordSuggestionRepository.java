package com.lihuel.discordbot.bots.fourpicsoneing.repository;

import com.lihuel.discordbot.bots.fourpicsoneing.entity.WordSuggestion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordSuggestionRepository extends MongoRepository<WordSuggestion, String> {
}
