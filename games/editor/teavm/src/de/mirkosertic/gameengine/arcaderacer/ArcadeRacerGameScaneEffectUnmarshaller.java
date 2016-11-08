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
package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSceneEffect;
import de.mirkosertic.gameengine.core.GameSceneEffectUnmarshaller;

import java.util.Map;

public class ArcadeRacerGameScaneEffectUnmarshaller implements GameSceneEffectUnmarshaller {

    @Override
    public String getTypeKey() {
        return ArcadeRacerGameSceneEffect.TYPE_VALUE;
    }

    @Override
    public GameSceneEffect unmarshall(GameRuntime aGameRuntime, GameScene aScene, Map<String, Object> aObjectData) {
        return ArcadeRacerGameSceneEffect.unmarshall(aGameRuntime, aScene, aObjectData);
    }
}
