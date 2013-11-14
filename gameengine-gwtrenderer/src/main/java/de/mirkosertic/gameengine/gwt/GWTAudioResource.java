package de.mirkosertic.gameengine.gwt;

import com.google.gwt.media.client.Audio;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

public class GWTAudioResource implements GameResource {

    private final Audio audio;

    public GWTAudioResource(Audio aAudio) {
        audio = aAudio;
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.SOUND;
    }

    public Audio getAudio() {
        return audio;
    }
}
