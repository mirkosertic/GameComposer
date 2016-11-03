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
package de.mirkosertic.gameengine.web.github;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.core.SuccessCallback;
import de.mirkosertic.gameengine.teavm.TeaVMGameLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMLoadedSpriteSheet;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.web.EditorProject;
import org.teavm.jso.ajax.XMLHttpRequest;

import java.io.IOException;

public class GithubEditorProject implements EditorProject {

    // http://mdswanson.com/blog/2011/07/23/digging-around-the-github-api-take-2.html
    // https://gist.github.com/robnyman/1894032

    private final String username;
    private final String repository;
    private final String relativePath;

    public GithubEditorProject(String aUsername, String aRepository, String aRelativePath) {
        username = aUsername;
        repository = aRepository;
        relativePath = aRelativePath;
    }

    @Override
    public TeaVMGameSceneLoader createSceneLoader(TeaVMGameSceneLoader.GameSceneLoadedListener aListener,
            AbstractGameRuntimeFactory aRuntimeFactory) {
        return new TeaVMGameSceneLoader(aListener, aRuntimeFactory) {
            @Override
            public void loadFromServer(Game aGame, String aSceneName, TeaVMGameResourceLoader aResourceLoader) {
                final XMLHttpRequest theRequest = XMLHttpRequest.create();
                theRequest.overrideMimeType("text/plain");
                theRequest.open("GET", "https://raw.githubusercontent.com/" + username + "/" + repository + "/master" + relativePath + "/" + aSceneName + "/scene.json");
                theRequest.onComplete(() -> listener.onGameSceneLoaded(parse(aGame, theRequest.getResponseText(), aResourceLoader)));
                theRequest.send();
            }
        };
    }

    @Override
    public TeaVMGameLoader createGameLoader(TeaVMGameLoader.GameLoadedListener aListener) {
        return new TeaVMGameLoader(aListener) {
            @Override
            public void loadFromServer() {
                final XMLHttpRequest theRequest = XMLHttpRequest.create();
                theRequest.overrideMimeType("text/plain");
                theRequest.open("GET", "https://raw.githubusercontent.com/" + username + "/" + repository + "/master" + relativePath + "/game.json");
                theRequest.onComplete(() -> listener.onGameLoaded(parse(theRequest.getResponseText())));
                theRequest.send();
            }
        };
    }

    @Override
    public TeaVMGameResourceLoader createResourceLoaderFor(String aSceneID) {
        return new TeaVMGameResourceLoader(aSceneID) {
            @Override
            public GameResource load(ResourceName aResourceName) throws IOException {
                ResourceName theNewResourceName = new ResourceName("https://raw.githubusercontent.com/" + username + "/" + repository + "/master" + relativePath + "/" + aSceneID + aResourceName.get());
                return convert(theNewResourceName);
            }

            @Override
            public LoadedSpriteSheet loadSpriteSheet(ResourceName aResourceName, SuccessCallback aCallback) {
                ResourceName theNewResourceName = new ResourceName("https://raw.githubusercontent.com/" + username + "/" + repository + "/master" + relativePath + "/" + aSceneID + aResourceName.get());
                return new TeaVMLoadedSpriteSheet(theNewResourceName, aCallback);
            }
        };
    }

    @Override
    public void openFileSystem(EditorProject.FilesystemCallback aCallback) {
        IndexedDBFilesystem.open("github_" + username + "/" + repository, new IndexedDBFilesystem.Callback() {
            @Override
            public void onError() {
                aCallback.onError(GithubEditorProject.this);
            }

            @Override
            public void onSuccess(IndexedDBFilesystem aFilesystem) {
                aCallback.onSuccess(GithubEditorProject.this, aFilesystem);
            }
        });
    }
}