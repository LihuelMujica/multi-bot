package com.lihuel.discordbot.bots.invisiblefriend.embeds;


import com.lihuel.discordbot.bots.invisiblefriend.entity.GameStatus;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendGame;
import com.lihuel.discordbot.bots.invisiblefriend.entity.InvisibleFriendUser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InvisibleFriendEmbed {

    public static @NotNull MessageEmbed GameInfoEmbed(InvisibleFriendGame game) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Amigo invisible");
        embedBuilder.setDescription("Partida de amigo invisible");
        embedBuilder.addField("Estado", mapStatus(game.getStatus()), false);
        List<List<InvisibleFriendUser>> groups = game.getGroups();
        if (groups != null && !groups.isEmpty()) {
            for (int i = 0; i < groups.size(); i++) {
                List<InvisibleFriendUser> group = groups.get(i);
                StringBuilder groupString = new StringBuilder();
                for (InvisibleFriendUser user : group) {
                    groupString.append("<@").append(user.getUserId()).append(">").append("\n");
                }
                embedBuilder.addField("Grupo " + (i), groupString.toString(), false);
            }
        }
        else {
            embedBuilder.addField("Grupos", "No hay grupos", false);
        }
        return embedBuilder.build();
    }

    private static String mapStatus(GameStatus status) {
        return switch (status) {
            case CREATED -> "Creada";
            case STARTED -> "Iniciada";
            case FINISHED -> "Finalizada";
            default -> "Desconocido";
        };
    }

    public static @NotNull MessageEmbed giftEmbed(String title, String body, String imgURL) {
        if (title == null || title.isEmpty()) {
            title = "Regalito de tu amigo invisible";
        }
        if (imgURL == null || imgURL.isEmpty()) {
            imgURL = "https://cdn.discordapp.com/attachments/829134660533616692/1190405649381736518/FkllZn7X0AAU56j.png?ex=65a1aea7&is=658f39a7&hm=425c69315f8406933f6a3fd02d8ef49156b0e6274caf6eae957b63bad65e8b84&";
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(body);
        embedBuilder.setImage(imgURL);
        return embedBuilder.build();
    }

    public static @NotNull MessageEmbed giftEmbed(String title, String body, String imgURL, String receiverId) {
        if (title == null || title.isEmpty()) {
            title = "Regalito de tu amigo invisible";
        }
        if (imgURL == null || imgURL.isEmpty()) {
            imgURL = "https://cdn.discordapp.com/attachments/829134660533616692/1190405649381736518/FkllZn7X0AAU56j.png?ex=65a1aea7&is=658f39a7&hm=425c69315f8406933f6a3fd02d8ef49156b0e6274caf6eae957b63bad65e8b84&";
        }
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setAuthor("Tu amigo invisible te envió un regalo", null, "https://cdn0.iconfinder.com/data/icons/small-n-flat/24/678132-gift-512.png");
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(body);
        embedBuilder.setImage(imgURL);
        embedBuilder.addField("", "P    ara <@" + receiverId + ">", true);
        return embedBuilder.build();
    }
}
