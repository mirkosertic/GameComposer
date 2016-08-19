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
package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;

public interface ContextMenuListener {

    void onDeleteGameScene(GameScene aGameScene);

    void onDeleteGameObject(GameObject aGameObject);

    void onDeleteGameObjectInstance(GameObjectInstance aGameObjectInstance);

    void onCreateNewGameObject(GameScene aGameScene);

    void onCreateNewGameScene();

    void onNewEventSheet(GameScene aGameScene);

    void onDeleteEventSheet(EventSheet aEventSheet);
}
