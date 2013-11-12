package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;
import javafx.scene.image.Image;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

public class JavaFXAudioResource implements GameResource {

    private DataLine.Info clipInfo;
    private Clip clip;

    JavaFXAudioResource(InputStream inputStream) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream theStream = AudioSystem.getAudioInputStream(inputStream);
        clipInfo = new DataLine.Info(Clip.class, theStream.getFormat());
        clip = (Clip) AudioSystem.getLine(clipInfo);
        clip.open(theStream);
    }

    Clip getClip() {
        return clip;
    }

    public GameResourceType getType() {
        return GameResourceType.SOUND;
    }
}
