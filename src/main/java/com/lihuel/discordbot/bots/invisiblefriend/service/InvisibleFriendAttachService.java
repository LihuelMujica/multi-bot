package com.lihuel.discordbot.bots.invisiblefriend.service;

import com.lihuel.discordbot.bots.invisiblefriend.entity.GameStatus;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendGame;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendPrivateAttach;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendUser;
import com.lihuel.discordbot.bots.invisiblefriend.exception.InvisibleFriendGameNotFoundException;
import com.lihuel.discordbot.bots.invisiblefriend.repository.InvisibleFriendAttachRepository;
import com.lihuel.discordbot.bots.invisiblefriend.repository.InvisibleFriendGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class InvisibleFriendAttachService {
    private final InvisibleFriendAttachRepository invisibleFriendAttachRepository;
    private final InvisibleFriendGameRepository invisibleFriendGameRepository;

    @Autowired
    public InvisibleFriendAttachService(InvisibleFriendAttachRepository invisibleFriendAttachRepository, InvisibleFriendGameRepository invisibleFriendGameRepository) {
        this.invisibleFriendAttachRepository = invisibleFriendAttachRepository;
        this.invisibleFriendGameRepository = invisibleFriendGameRepository;
    }

    public void attach(String guildId, String userId, String attach) throws InvisibleFriendGameNotFoundException {
        InvisibleFriendGame invisibleFriendGame = invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.STARTED).orElseThrow(() -> new InvisibleFriendGameNotFoundException("No hay ninguna partida en curso para este servidor"));
        InvisibleFriendUser invisibleFriendUser = invisibleFriendGame.getGroups().stream().flatMap(Collection::stream).filter(user -> user.getUserId().equals(userId)).findFirst().orElseThrow(() -> new InvisibleFriendGameNotFoundException("El usuario <@" + userId + "> no está en el juego"));
        if (invisibleFriendUser.getGiftSent() == null || invisibleFriendUser.getGiftSent().getBody().isEmpty()) {
            throw new InvisibleFriendGameNotFoundException("Debes enviar un regalo con /amigo-invisible-enviar-regalo  primero");
        }
        InvisibleFriendPrivateAttach invisibleFriendPrivateAttach = new InvisibleFriendPrivateAttach();
        invisibleFriendPrivateAttach.setGameId(invisibleFriendGame.getGameId());
        invisibleFriendPrivateAttach.setReceiverId(invisibleFriendUser.getGiftSent().getReceiverId());
        invisibleFriendPrivateAttach.setAttach(attach);
        invisibleFriendAttachRepository.save(invisibleFriendPrivateAttach);
    }

    public List<InvisibleFriendPrivateAttach> getAttach(String guildId, String receiverId) throws InvisibleFriendGameNotFoundException {
        InvisibleFriendGame invisibleFriendGame = invisibleFriendGameRepository.findByGuildIdAndStatus(guildId, GameStatus.STARTED).orElseThrow(() -> new InvisibleFriendGameNotFoundException("No hay ninguna partida en curso para este servidor"));
        List<InvisibleFriendPrivateAttach> invisibleFriendPrivateAttachList = invisibleFriendAttachRepository.findByGameIdAndReceiverId(invisibleFriendGame.getGameId(), receiverId);
        if (invisibleFriendPrivateAttachList.isEmpty()) {
            throw new InvisibleFriendGameNotFoundException("No hay ningún regalo privado para ti");
        }
        return invisibleFriendPrivateAttachList;
    }
}
