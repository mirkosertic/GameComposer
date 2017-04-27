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
package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.core.BehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public class ConstantMovementBehaviorTemplateUnmarshaller implements BehaviorTemplateUnmarshaller<ConstantMovementBehaviorTemplate> {

    @Override
    public BehaviorType getTypeKey() {
        return ConstantMovementBehavior.TYPE;
    }

    @Override
    public ConstantMovementBehaviorTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return ConstantMovementBehaviorTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
