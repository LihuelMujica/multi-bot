package com.lihuel.discordbot.bots.fourpicsoneing.service;

import com.lihuel.discordbot.bots.fourpicsoneing.entity.FourWordsGameConfig;
import com.lihuel.discordbot.bots.fourpicsoneing.entity.Word;
import com.lihuel.discordbot.bots.fourpicsoneing.entity.WordSuggestion;
import com.lihuel.discordbot.bots.fourpicsoneing.events.WordChangeEvent;
import com.lihuel.discordbot.bots.fourpicsoneing.repository.FourWordsGameConfigRepository;
import com.lihuel.discordbot.bots.fourpicsoneing.repository.WordRepository;
import com.lihuel.discordbot.bots.fourpicsoneing.repository.WordSuggestionRepository;
import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class WordService {
    private final WordRepository wordRepository;
    private final FourWordsGameConfigRepository configRepository;
    private final WordSuggestionRepository suggestionRepository;
    private final Map<String, FourWordsGameConfig> cachedGuilds = new HashMap<>();
    private final ApplicationContext applicationContext;

    @Autowired
    public WordService(WordRepository wordRepository, FourWordsGameConfigRepository configRepository, WordSuggestionRepository suggestionRepository, ApplicationContext applicationContext) {
        this.wordRepository = wordRepository;
        this.configRepository = configRepository;
        this.suggestionRepository = suggestionRepository;
        this.applicationContext = applicationContext;
    }

    public Word getRandomWord(String guildId) {
        Word word = wordRepository.getRandomWord(guildId);
        if (word == null) {
            return null;
        }
        if (word.getPics() == null) {
            throw new RuntimeException("La palabra no tiene imágenes");
        }
        if (word.getPics().getGroupedPic() == null)
            throw new RuntimeException("La palabra no tiene imágenes agrupadas");
        return word;
    }

    public String getGameChannel(String guildId) {
        if (cachedGuilds.containsKey(guildId)) {
            return cachedGuilds.get(guildId).getGameChannelId();
        }
        FourWordsGameConfig config = configRepository.findById(guildId).orElse(null);
        if (config == null) {
            throw new RuntimeException("No se encontró la configuración del juego");
        }
        cachedGuilds.put(guildId, config);
        return config.getGameChannelId();
    }

    public FourWordsGameConfig getGuildImage(String guildId) {
        FourWordsGameConfig config = configRepository.findById(guildId).orElse(null);
        if (config == null) {
            throw new RuntimeException("No se encontró la configuración del juego");
        }
        return config;
    }

    /**
     * Starts the game in the specified channel.
     * @param guildId The guild id
     * @param channelId The channel id
     * @return The game config. This contains the current word.
     */
    public FourWordsGameConfig startGame(String guildId, String channelId) {
        FourWordsGameConfig config = configRepository.findById(guildId).orElse(new FourWordsGameConfig());
        config.setGuildId(guildId);
        config.setGameChannelId(channelId);
        Word word = getRandomWord(guildId);
        config.setCurrentWord(word);
        configRepository.save(config);
        cachedGuilds.put(guildId, config);
        applicationContext.publishEvent(new WordChangeEvent(config, null));
        return config;
    }

    public void sendAnswer(String guildId, Message message, String answer) {
        FourWordsGameConfig config = configRepository.findById(guildId).orElse(null);
        System.out.println(config);
        System.out.println("Answer: " + answer);
        System.out.println("GuildID: " + guildId);
        if (config == null) {
            throw new RuntimeException("No se encontró la configuración del juego");
        }
        if (config.getCurrentWord() == null || !config.getCurrentWord().getWord().equalsIgnoreCase(answer)) {
            return;
        }
        if (Objects.equals(config.getCurrentWord().getAuthorId(), message.getAuthor().getId())) {
            return;
        }
        Word oldWord = config.getCurrentWord();
        oldWord.getPlayedGuilds().add(guildId);
        wordRepository.save(oldWord);
        Word newWord = getRandomWord(guildId);
        config.setCurrentWord(newWord);
        config.addScore(message.getAuthor().getId(), applicationContext);
        configRepository.save(config);
        applicationContext.publishEvent(new WordChangeEvent(config, message));
    }

    public void sendSuggestion(WordSuggestion suggestion) {
        suggestionRepository.save(suggestion);
    }

    public void resetServerWords(String guildId) {
        List<Word> words = wordRepository.getAllWordsSended(guildId);
        System.out.println(words);
        words.forEach(word -> {
            word.setPlayedGuilds(
                    word.getPlayedGuilds().stream()
                            .filter(s -> !s.equals(guildId))
                            .toList()
            );
        });
        wordRepository.saveAll(words);

        FourWordsGameConfig config = configRepository.findById(guildId).orElse(null);
        if (config != null) {
            startGame(guildId, config.getGameChannelId());
        }}


}
