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
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;
import java.io.InputStream;

public abstract class JavaFXAbstractGameResourceLoader implements GameResourceLoader {

    protected abstract InputStream getInputStreamFor(ResourceName aResourceName) throws IOException;

    @Override
    public Promise<GameResource, String> load(ResourceName aResourceName) {
        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> {
            try {
                InputStream theStream = getInputStreamFor(aResourceName);
                if (theStream == null) {
                    aRejector.reject("Not found : " + aResourceName.name, null);
                    return;
                }
                if (aResourceName.name.toLowerCase().endsWith(".png")) {
                    aResolver.resolve(new JavaFXBitmapResource(theStream));
                }

                if (aResourceName.name.toLowerCase().endsWith(".au")) {
                    aResolver.resolve(new JavaFXAudioResource(theStream));
                }
                if (aResourceName.name.toLowerCase().endsWith(".wav")) {
                    aResolver.resolve(new JavaFXAudioResource(theStream));
                }
            } catch (IOException e) {
                aRejector.reject(e.getMessage(), e);
            } catch (Exception e) {
                aRejector.reject(e.getMessage(), e);
            }
        });
    }

    @Override
    public void flush() {
    }

    @Override
    public Promise<LoadedSpriteSheet, String> loadSpriteSheet(ResourceName aResourceName) {
        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> aResolver.resolve(LoadedSpriteSheet.EMPTY));
    }
}