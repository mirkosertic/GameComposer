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
package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.ConditionUnmarshaller;
import de.mirkosertic.gameengine.core.GameScene;

import java.util.Map;

public class ObjectCollisionConditionUnmarshaller implements ConditionUnmarshaller {

    @Override
    public String getTypeKey() {
        return ObjectCollisionCondition.TYPE_VALUE;
    }

    @Override
    public Condition unmarshall(GameScene aGameScene, Map<String, Object> aSerializedData) {
        return ObjectCollisionCondition.unmarshall(aGameScene, aSerializedData);
    }
}
