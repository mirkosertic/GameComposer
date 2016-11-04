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

import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;
import java.io.InputStream;

public abstract class JavaFXAbstractGameResourceLoader implements GameResourceLoader {

    protected abstract InputStream getInputStreamFor(ResourceName aResourceName) throws IOException;

    @Override
    public void load(ResourceName aResourceName, Listener aListener) throws IOException {
        InputStream theStream = getInputStreamFor(aResourceName);
        if (theStream == null) {
            return;
        }
        if (aResourceName.name.toLowerCase().endsWith(".png")) {
            aListener.handle(new JavaFXBitmapResource(theStream));
        }
        try {
            if (aResourceName.name.toLowerCase().endsWith(".au")) {
                aListener.handle(new JavaFXAudioResource(theStream));
            }
            if (aResourceName.name.toLowerCase().endsWith(".wav")) {
                aListener.handle(new JavaFXAudioResource(theStream));
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public void loadSpriteSheet(ResourceName aResourceName, SpritesheetListener aListener) {
        aListener.handle(LoadedSpriteSheet.EMPTY);
    }
}