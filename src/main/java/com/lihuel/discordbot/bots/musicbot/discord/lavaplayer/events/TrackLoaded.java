package com.lihuel.discordbot.bots.musicbot.discord.lavaplayer.events;

import com.lihuel.discordbot.bots.musicbot.discord.lavaplayer.Track;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event that is fired when a track is loaded
 */
@Getter
public class TrackLoaded extends ApplicationEvent {
    private final Track track;

    public TrackLoaded(Track track) {
        super(track);
        this.track = track;
    }

}
