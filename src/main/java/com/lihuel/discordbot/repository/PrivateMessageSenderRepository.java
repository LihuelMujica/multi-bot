package com.lihuel.discordbot.repository;

import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PrivateMessageSenderRepository {
    private final Map<String, PrivateMessage> messagesRegistry = new HashMap<>();

    public void createMessageModal(String modalID, List<String> userIds, String botId) {
        if (messagesRegistry.size() > 100) {
            messagesRegistry.clear();
        }
        messagesRegistry.put(modalID, PrivateMessage.builder()
                .userIds(userIds)
                .botId(botId)
                .build());
    }
    public PrivateMessage getMessageDataByModalID(String modalID) {
        return messagesRegistry.get(modalID);
    }

    @Data
    @Builder
    public static class PrivateMessage {
        private List<String> userIds;
        private String botId;
    }
}
