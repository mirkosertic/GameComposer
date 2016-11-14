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
package de.mirkosertic.gameengine.web;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.teavm.TeaVMGameResourceLoader;
import de.mirkosertic.gameengine.teavm.TeaVMGameSceneLoader;
import de.mirkosertic.gameengine.teavm.TeaVMMap;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;
import org.teavm.jso.json.JSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditorState {

    public interface LoadingListener {

        void onGameLoaded(EditorState aState);

        void onGameLoadingError(Throwable aThrowable);

        void onSceneLoaded(EditorState aState, String aSceneID);

        void onSceneLoadingError(EditorState aState, String aSceneID, Throwable aThrowable);
    }

    private Game loadedGame;
    private final Map<String, GameScene> loadedScenes;
    private final EditorProject editorProject;
    private final AbstractGameRuntimeFactory runtimeFactory;
    private final ResourceAccessor resourceAccessor;

    public EditorState(EditorProject aProject, AbstractGameRuntimeFactory aRuntimeFactory, ResourceAccessor aResourceAccessor) {
        loadedScenes = new HashMap<>();
        editorProject = aProject;
        runtimeFactory = aRuntimeFactory;
        resourceAccessor = aResourceAccessor;
    }

    public EditorProject getEditorProject() {
        return editorProject;
    }

    public AbstractGameRuntimeFactory getRuntimeFactory() {
        return runtimeFactory;
    }

    public Game getLoadedGame() {
        return loadedGame;
    }

    public GameScene getGameSceneById(String aSceneId) {
        return loadedScenes.get(aSceneId);
    }

    public Promise<File, String> saveGame() {
        JSObject theJSForm = TeaVMMap.toJS(loadedGame.serialize());
        String theJSON = JSON.stringify(theJSForm);

        Blob theBlob = Blob.createJSONBlob(JSString.valueOf(theJSON));
        return resourceAccessor.persistFile("/game.json", theBlob);
    }

    private String getIDForScene(GameScene aGameScene) {
        for (Map.Entry<String, GameScene> theEntry : loadedScenes.entrySet()) {
            if (theEntry.getValue() == aGameScene) {
                return theEntry.getKey();
            }
        }
        throw new IllegalStateException("Unknown game scene");
    }

    public Promise<GameScene, String> saveScene(GameScene aScene) {
        Promise<File, String> theScenePromise = new Promise<>((Promise.Executor) (aResolver, aRejector) -> {
            JSObject theJSForm = TeaVMMap.toJS(aScene.serialize());
            String theJSON = JSON.stringify(theJSForm);
            Blob theBlob = Blob.createJSONBlob(JSString.valueOf(theJSON));

            resourceAccessor.persistFile("/" + getIDForScene(aScene) + "/scene.json", theBlob).thenContinue(aResolver::resolve).catchError(
                    (aResult, aOptionalRejectedException) -> aRejector.reject(aResult, aOptionalRejectedException));
        });

        Promise<GameScene, String> theResult = new Promise<>();

        Promise<File, String> theGamePromise = saveGame();
        Promise<Promise[], Void> theAll = Promise.all(theScenePromise, theGamePromise);
        theAll.thenContinue(aResult -> {
            theResult.resolve(aScene);
        }).catchError((aResult, aOptionalRejectedException) -> theResult.reject("Error while saving data", aOptionalRejectedException));

        return theResult;
    }

    public void load(LoadingListener aListener) {
        resourceAccessor.createGameLoader().loadFromServer().thenContinue(
                aResult -> {
                    loadedGame = aResult;
                    aListener.onGameLoaded(EditorState.this);
                    loadScenes(aListener);
                }).catchError((aResult, aOptionalException) -> aListener.onGameLoadingError(aOptionalException));
    }

    private void loadScenes(LoadingListener aLoadingListener) {
        String[] theKnownScenes = loadedGame.getKnownScenes();
        for (String theScene : theKnownScenes) {
            TeaVMGameResourceLoader theLoader = resourceAccessor.createResourceLoaderFor(theScene);
            TeaVMGameSceneLoader theSceneLoader = resourceAccessor.createSceneLoader(runtimeFactory);
            theSceneLoader.loadFromServer(loadedGame, theScene, theLoader).thenContinue(aResult -> {
                loadedScenes.put(theScene, aResult);
                aLoadingListener.onSceneLoaded(EditorState.this, theScene);
            }).catchError((aResult, aOptionalException) -> aLoadingListener.onSceneLoadingError(EditorState.this, theScene, aOptionalException));
        }
    }

    public String[] getLoadedScenes() {
        List<String> theKeys = new ArrayList<>(loadedScenes.keySet());
        return theKeys.toArray(new String[theKeys.size()]);
    }
}