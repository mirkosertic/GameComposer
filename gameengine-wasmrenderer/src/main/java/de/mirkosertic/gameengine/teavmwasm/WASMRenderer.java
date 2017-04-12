/*
 * Copyright 2017 Mirko Sertic
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
package de.mirkosertic.gameengine.teavmwasm;

import java.util.Map;

import de.mirkosertic.gameengine.type.PositionAnchor;
import org.teavm.interop.Export;
import org.teavm.interop.Import;

import de.mirkosertic.gameengine.Version;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;

public class WASMRenderer {

    private static Game loadedGame;

    @Import(module = "engine", name = "bootstrap")
    public static native void bootstrap();

    @Import(module = "engine", name = "loadGameScene")
    public static native void loadGameScene(String aGameSceneId);

    @Export(name = "loadGameFromStringPool")
    public static void loadGameFromStringPool(int aPoolID) {
        Map<String, Object> theResult = new JSONParser().fromJSON(WASMStringPool.getStringPool(aPoolID));

        loadedGame = Game.deserialize(theResult);
        WASMLogger.INSTANCE.info("Game loaded : " + loadedGame.nameProperty().get());

        String theStartingScene = loadedGame.defaultSceneProperty().get();
        WASMLogger.INSTANCE.info("Loading default scene " + theStartingScene);

        loadGameScene(theStartingScene);
    }

    @Export(name = "loadGameSceneFromStringPool")
    public static void loadGameSceneFromStringPool(int aPoolID) {
        try {

            Map<String, Object> theResult = new JSONParser().fromJSON(WASMStringPool.getStringPool(aPoolID));

            WASMLogger.INSTANCE.info("Loading scene");

            WASMGameRuntimeFactory runtimeFactory = new WASMGameRuntimeFactory();
            GameRuntime theRuntime = runtimeFactory.create(new WASMGameResourceLoader(), new WASMGameSoundSystemFactory());
            WASMLogger.INSTANCE.info("Runtime created");

            GameScene theScene = GameScene.deserialize(loadedGame, theRuntime, theResult);

            WASMLogger.INSTANCE.info("Ok");
            //WASMLogger.log("Scene loaded : " + theScene.nameProperty().get());
        } catch (Exception e) {
            WASMLogger.INSTANCE.error(e.getMessage());
        }

    }

    public static void main(String[] args) {
        WASMLogger.INSTANCE.info("Gameengine starting with version " + Version.VERSION);

        WASMStringPool theStringPool = new WASMStringPool();

        try {
            PositionAnchor a = PositionAnchor.valueOf("SCENE");
            WASMLogger.INSTANCE.info("Found : " + a.name());
        } catch (Exception e) {
            WASMLogger.INSTANCE.error("Error while searching");
        }

        bootstrap();
        // We are done here, waiting fpor the loadGameFromStringPool callback invoked
        // by JavaScript side
    }
}
