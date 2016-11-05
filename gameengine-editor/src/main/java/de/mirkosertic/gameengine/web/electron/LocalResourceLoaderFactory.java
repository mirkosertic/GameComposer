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
package de.mirkosertic.gameengine.web.electron;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMLoadedSpriteSheet;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.web.ResourceLoaderFactory;
import de.mirkosertic.gameengine.web.electron.fs.FS;

import java.io.IOException;

public class LocalResourceLoaderFactory implements ResourceLoaderFactory {

    private final FS fs;
    private final String localPath;

    public LocalResourceLoaderFactory(FS aFS, String aLocalPath) {
        fs = aFS;
        localPath = aLocalPath;
    }

    @Override
    public TeaVMGameSceneLoader createSceneLoader(TeaVMGameSceneLoader.GameSceneLoadedListener aListener,
            AbstractGameRuntimeFactory aRuntimeFactory) {
        return new TeaVMGameSceneLoader(aListener, aRuntimeFactory) {
            @Override
            public void loadFromServer(Game aGame, String aSceneName, TeaVMGameResourceLoader aResourceLoader) {
                String theFile = localPath + "/" + aSceneName + "/scene.json";
                String theData = fs.readFileSync(theFile, "utf8");

                GameScene theScene = parse(aGame, theData, aResourceLoader);
                listener.onGameSceneLoaded(theScene);
            }
        };
    }

    @Override
    public TeaVMGameLoader createGameLoader(TeaVMGameLoader.GameLoadedListener aListener) {
        return new TeaVMGameLoader(aListener) {
            @Override
            public void loadFromServer() {
                String theFile = localPath + "/game.json";
                String theData = fs.readFileSync(theFile, "utf8");
                Game theGame = parse(theData);
                listener.onGameLoaded(theGame);
            }
        };
    }

    @Override
    public TeaVMGameResourceLoader createResourceLoaderFor(String aSceneID) {
        return new TeaVMGameResourceLoader(aSceneID) {
            @Override
            public void load(ResourceName aResourceName, Listener aListener) throws IOException {

                String theFile = localPath + "/" + aSceneID + aResourceName.name;
                theFile = theFile.replace('\\', '/');
                if (!theFile.startsWith("/")) {
                    theFile = "/" + theFile;
                }

                String theURL = "file://" + theFile;

                ResourceName theNewResourceName = new ResourceName(theURL);
                aListener.handle(convert(theNewResourceName, theNewResourceName));
            }

            @Override
            public void loadSpriteSheet(ResourceName aResourceName, SpritesheetListener aListener) {

                String theFile = localPath + "/" + aSceneID + aResourceName.name;
                theFile = theFile.replace('\\', '/');
                if (!theFile.startsWith("/")) {
                    theFile = "/" + theFile;
                }

                String theURL = "file://" + theFile;

                ResourceName theNewResourceName = new ResourceName(theURL);
                new TeaVMLoadedSpriteSheet(theNewResourceName, aSpriteSheet -> aListener.handle(aSpriteSheet));
            }
        };
    }
}