package com.lihuel.discordbot.discord.commands;

import com.lihuel.discordbot.annotations.SlashCommand;
import com.lihuel.discordbot.discord.JDAFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SlashCommand({"invocador"})
public class SendPrivateMessagesToRole implements Command {

    private final JDAFactory jdaFactory;

    @Autowired
    public SendPrivateMessagesToRole(JDAFactory jdaFactory) {
        this.jdaFactory = jdaFactory;
    }

    @Override
    public String getName() {
        return "mensaje-privado-a-role";
    }

    @Override
    public String getDescription() {
        return "Envía un mensaje privado a todos los usuarios con un rol específico";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(
                new OptionData(OptionType.ROLE, "rol", "Rol al que enviar el mensaje", true),
                new OptionData(OptionType.USER, "bot", "Bot que enviará el mensaje", true),
                new OptionData(OptionType.STRING, "mensaje", "Mensaje a enviar", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) throws Exception {
        Role role = event.getOption("rol").getAsRole();
        String message = event.getOption("mensaje").getAsString();
        String guildID = event.getGuild().getId();
        JDA bot = jdaFactory.getJdaById(event.getOption("bot").getAsUser().getId());
        System.out.println(bot);
        if (bot == null) {
            event.reply("El bot no está disponible o no existe").setEphemeral(true).queue();
            return;
        }
        bot.getGuildById(guildID).findMembersWithRoles(role).onSuccess(members -> {
            members.forEach(user -> {
                user.getUser().openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage(message).queue();
                });
            });
        }).onError(error -> {
            event.reply("Ha ocurrido un error al enviar los mensajes").setEphemeral(true).queue();
        });
        event.reply("Enviando mensajes...").setEphemeral(true).queue();

    }
}
