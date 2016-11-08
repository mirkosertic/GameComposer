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
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.network.DefaultNetworkConnector;
import de.mirkosertic.gameengine.teavm.pixi.Renderer;
import de.mirkosertic.gameengine.type.Size;

public class GameSceneEditorPlaySceneStrategy extends PlaySceneStrategy {

    private final SceneEditorHTMLElement sceneEditor;
    private final Renderer renderer;
    private GameEditorGameView gameView;

    public GameSceneEditorPlaySceneStrategy(AbstractGameRuntimeFactory aRuntimeFactory, GameLoopFactory aGameLoopFactory, SceneEditorHTMLElement aSceneEditor, Renderer aRenderer) {
        super(aRuntimeFactory, aGameLoopFactory, new DefaultNetworkConnector());
        sceneEditor = aSceneEditor;
        renderer = aRenderer;
    }

    @Override
    protected void loadOtherScene(String aSceneId) {
    }

    @Override
    protected Size getScreenSize() {
        return new Size(sceneEditor.currentCanvas().getClientWidth(), sceneEditor.currentCanvas().getClientHeight());
    }

    @Override
    protected GameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector) {
        if (gameView == null) {
            gameView = new GameEditorGameView(aGameRuntime, aCamera, aGestureDetector, renderer);
            sceneEditor.gridWidth().setValue(Integer.toString(gameView.gridWidth().get()));
            sceneEditor.gridHeight().setValue(Integer.toString(gameView.gridHeight().get()));

            sceneEditor.gridWidth().addEventListener("change", evt -> gameView.gridWidth().set(Integer.valueOf(sceneEditor.gridWidth().getValue())));
            sceneEditor.gridHeight().addEventListener("change", evt -> gameView.gridHeight().set(Integer.valueOf(sceneEditor.gridHeight().getValue())));
        } else {
            gameView.prepareNewScene(aGameRuntime, aCamera, aGestureDetector);
        }
        gameView.setSize(getScreenSize());
        return gameView;
    }

    @Override
    public void handleResize() {
        Size theCurrentSize = getScreenSize();
        getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new SetScreenResolution(theCurrentSize));
        gameView.setSize(theCurrentSize);
    }
}