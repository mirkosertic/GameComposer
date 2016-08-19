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

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.process.GameProcess;

public class ArcadeRacerGameSceneProcess implements GameProcess {

    private final ArcadeRacerGameSceneEffect effect;

    public ArcadeRacerGameSceneProcess(ArcadeRacerGameSceneEffect aEffect) {
        effect = aEffect;
    }

    @Override
    public void started() {

    }

    @Override
    public boolean affectsInstance(GameObjectInstance aInstance) {
        return false;
    }

    @Override
    public ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        return effect.proceesGame(aGameTime, aElapsedTimeSinceLastLoop);
    }

    @Override
    public void killed() {

    }

    @Override
    public GameProcess getChildProcess() {
        return null;
    }
}
