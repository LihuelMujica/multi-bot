package com.lihuel.discordbot.bots.invisiblefriend.service;


import com.lihuel.discordbot.bots.invisiblefriend.entity.GameStatus;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendGame;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendGift;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendUser;
import com.lihuel.discordbot.bots.invisiblefriend.exception.InvisibleFriendGameAlreadyExistsException;
import com.lihuel.discordbot.bots.invisiblefriend.exception.InvisibleFriendGameNotFoundException;
import com.lihuel.discordbot.bots.invisiblefriend.repository.InvisibleFriendGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InvisibleFriendGameService {
    private final InvisibleFriendGameRepository invisibleFriendGameRepository;

    @Autowired
    public InvisibleFriendGameService(InvisibleFriendGameRepository invisibleFriendGameRepository) {
        this.invisibleFriendGameRepository = invisibleFriendGameRepository;
    }

    public InvisibleFriendGame createGame(String guildId) throws InvisibleFriendGameAlreadyExistsException {
        if (
                invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.CREATED).isPresent() ||
                invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.STARTED).isPresent()) {
            throw new InvisibleFriendGameAlreadyExistsException("Ya existe un juego creado para este servidor");
        }

        InvisibleFriendGame invisibleFriendGame = new InvisibleFriendGame();
        invisibleFriendGame.setGuildId(guildId);
        invisibleFriendGame.setStatus(GameStatus.CREATED);
        return invisibleFriendGameRepository.save(invisibleFriendGame);
    }

    public InvisibleFriendGame getGame(String guildId) throws InvisibleFriendGameNotFoundException {
        InvisibleFriendGame invisibleFriendGame = invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.CREATED).orElse(invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.STARTED).orElse(null));
        if (invisibleFriendGame == null) {
            throw new InvisibleFriendGameNotFoundException("No hay ninguna partida creada o en curso para este servidor");
        }
        return invisibleFriendGame;
    }
    public InvisibleFriendGame addGroup(String guildId, List<String> userIds) throws InvisibleFriendGameNotFoundException, InvisibleFriendGameAlreadyExistsException {
        InvisibleFriendGame invisibleFriendGame = invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.CREATED).orElseThrow(() -> new InvisibleFriendGameNotFoundException("No hay ninguna partida creada para este servidor o la partida ya ha comenzado"));
        List<InvisibleFriendUser> users = new ArrayList<>();
        for (String userId : userIds) {
            if (invisibleFriendGameRepository.playerIsInGame(guildId, userId)) {
                throw new InvisibleFriendGameAlreadyExistsException("El usuario <@" + userId + "> ya está en el juego");
            }
            InvisibleFriendUser invisibleFriendUser = new InvisibleFriendUser();
            invisibleFriendUser.setUserId(userId);
            users.add(invisibleFriendUser);
        }
        invisibleFriendGame.getGroups().add(users);
        return invisibleFriendGameRepository.save(invisibleFriendGame);
    }

    public InvisibleFriendGame removeGroup(String guildId, int groupId) throws InvisibleFriendGameNotFoundException {
        InvisibleFriendGame game = invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.CREATED).orElseThrow(() -> new InvisibleFriendGameNotFoundException("No hay ninguna partida creada para este servidor o la partida ya ha comenzado"));
        game.getGroups().remove(groupId);
        return invisibleFriendGameRepository.save(game);
    }
    public InvisibleFriendGame startGame(String guildId) throws InvisibleFriendGameNotFoundException {
        InvisibleFriendGame game = invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.CREATED).orElseThrow(() -> new InvisibleFriendGameNotFoundException("No hay ninguna partida creada para este servidor o la partida ya ha comenzado"));
        List<List<InvisibleFriendUser>> groups = game.getGroups();
        for (List<InvisibleFriendUser> group : groups) {
            assignFriends(group);
        }
        game.setStatus(GameStatus.STARTED);
        return invisibleFriendGameRepository.save(game);
    }

    private void assignFriends(List<InvisibleFriendUser> users) {
        List<InvisibleFriendUser> unassignedUsers = new ArrayList<>(List.copyOf(users));
        for (int i = 0; i < users.size() ; i++) {
            InvisibleFriendUser user = users.get(i);
            InvisibleFriendUser friend = unassignedUsers.get( (int) (Math.random() * unassignedUsers.size()));
            if (user.getUserId().equals(friend.getUserId())) {
                i--;
                continue;
            }
            user.setFriendId(friend.getUserId());
            unassignedUsers.remove(friend);
        }
    }

    public InvisibleFriendGame endGame(String guildId) throws InvisibleFriendGameNotFoundException {
        InvisibleFriendGame game = invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.STARTED).orElse(
                invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.CREATED).orElse(null)
        );
        if (game == null) {
            throw new InvisibleFriendGameNotFoundException("No hay ninguna partida creada o en curso para este servidor");
        }
        game.setStatus(GameStatus.FINISHED);
        return invisibleFriendGameRepository.save(game);
    }

    public InvisibleFriendGame sendGift(String guildId, String userId, InvisibleFriendGift gift) throws InvisibleFriendGameNotFoundException {
        InvisibleFriendGame game = invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.STARTED).orElseThrow(() -> new InvisibleFriendGameNotFoundException("No hay ninguna en curso para este servidor"));
        for (List<InvisibleFriendUser> group : game.getGroups()) {
            for (InvisibleFriendUser user : group) {
                if (user.getUserId().equals(userId)) {
                    gift.setReceiverId(user.getFriendId());
                    user.setGiftSent(gift);
                    return invisibleFriendGameRepository.save(game);
                }
            }
        }
        throw new InvisibleFriendGameNotFoundException("El usuario <@" + userId + "> no está en la partida");
    }

    public List<InvisibleFriendGift> getGifts(String guildId) throws InvisibleFriendGameNotFoundException {
        InvisibleFriendGame game = getGame(guildId);
        List<InvisibleFriendGift> gifts = new ArrayList<>();
        System.out.println(game.getGroups());
        game.getGroups().forEach(group -> group.forEach(user -> {
            if (user.getGiftSent() == null ) {
                InvisibleFriendGift gift = new InvisibleFriendGift();
                gift.setReceiverId(user.getFriendId());
                gift.setTitle("EL VERDADERO AMIGO INVISIBLE");
                gift.setBody("El verdadero amigo invisible es el que no te regala nada. Lo vamos a funar en Twitter.");
                gift.setImgURL("https://imgs.search.brave.com/qBt8iHMCzCFRi_anS5vBW3LIeQOGKSgXHorS-DO7AdU/rs:fit:860:0:0/g:ce/aHR0cHM6Ly93d3cu/ZWx0aWVtcG8uY29t/L2ZpbGVzL2FydGlj/bGVfbWFpbl8xMjAw/L3VwbG9hZHMvMjAy/My8wMi8wNi82M2Ux/NzZiNDBjYWY5LnBu/Zw");
                gifts.add(gift);
                return;
            }
            gifts.add(user.getGiftSent());
        }));
        return gifts;
    }

    public void resetGifts(String guildId) throws InvisibleFriendGameNotFoundException {
        InvisibleFriendGame game = getGame(guildId);
        game.getGroups().forEach(group -> group.forEach(user -> user.setGiftSent(null)));
        invisibleFriendGameRepository.save(game);
    }

}
