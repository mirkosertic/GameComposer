/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.apache.commons.io.IOUtils;

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

    @Override
    public GameResourceType getType() {
        return GameResourceType.SOUND;
    }
}
