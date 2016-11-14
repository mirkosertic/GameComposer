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
package de.mirkosertic.gameengine.gwt;

import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.type.ResourceName;

public class GWTGameResourceLoader implements GameResourceLoader {

    private static final String holderId = "resourcecache";

    private final String baseDirectory;

    public GWTGameResourceLoader(String aBaseDirectory) {
        baseDirectory = aBaseDirectory;
    }

    @Override
    public Promise<GameResource, String> load(ResourceName aResourceName) {

        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> {
            if (aResourceName.name.endsWith(".png")) {
                Image theImage = new Image();
                theImage.setVisible(false);
                theImage.setUrl(baseDirectory + aResourceName.name.replace('\\', '/'));
                RootPanel.get(holderId).add(theImage);
                aResolver.resolve(new GWTBitmapResource(theImage));
            }
            if (aResourceName.name.endsWith(".wav")) {
                Audio theAudio = Audio.createIfSupported();
                theAudio.setSrc(baseDirectory + aResourceName.name.replace('\\', '/'));
                aRejector.reject(new GWTAudioResource(theAudio), null);
            }
        });
    }

    @Override
    public Promise<LoadedSpriteSheet, String> loadSpriteSheet(ResourceName aResourceName) {
        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> aResolver.resolve(LoadedSpriteSheet.EMPTY));
    }

    @Override
    public void flush() {
        RootPanel.get(holderId).clear();
    }
}
