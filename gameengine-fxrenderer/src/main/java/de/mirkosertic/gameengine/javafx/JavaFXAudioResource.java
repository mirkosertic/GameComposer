package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

import org.apache.commons.io.IOUtils;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JavaFXAudioResource implements GameResource {

    private byte[] data;

    JavaFXAudioResource(InputStream inputStream) throws IOException {
        data = IOUtils.toByteArray(inputStream);
    }

    Clip createClip() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        AudioInputStream theStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(data));
        DataLine.Info theClipInfo = new DataLine.Info(Clip.class, theStream.getFormat());
        Clip theClip = (Clip) AudioSystem.getLine(theClipInfo);
        theClip.open(theStream);
        return theClip;
    }

    public GameResourceType getType() {
        return GameResourceType.SOUND;
    }
}
