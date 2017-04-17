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

import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.Speed;

import java.util.HashMap;
import java.util.Map;

public class ConstantMovementBehaviorTemplate implements BehaviorTemplate<ConstantMovementBehavior>, ConstantMovement, Reflectable<ConstantMovementClassInformation> {

    public static final BehaviorType TYPE = new BehaviorType("ID");

    private static final ConstantMovementClassInformation CIINSTANCE = new ConstantMovementClassInformation();

    private final Property<Speed> speed;
    private final Property<Speed> rotationSpeed;
    private final GameObject owner;

    public ConstantMovementBehaviorTemplate(GameEventManager aEventManager, GameObject aOwner) {
        speed = new Property<>(Speed.class, this, SPEED_PROPERTY, new Speed(0), aEventManager);
        rotationSpeed = new Property<>(Speed.class, this, ROTATIONSPEED_PROPERTY, new Speed(0), aEventManager);
        owner = aOwner;
    }

    @Override
    public BehaviorType getType() {
        return TYPE;
    }

    @Override
    public ConstantMovementClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public ConstantMovementBehavior create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        ConstantMovementBehavior theResult = new ConstantMovementBehavior(aInstance);
        theResult.registerEvents(aGameRuntime);
        return theResult;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    @Override
    public Property<Speed> speedProperty() {
        return speed;
    }

    @Override
    public Property<Speed> rotationSpeedProperty() {
        return rotationSpeed;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(ConstantMovementBehavior.TYPE_ATTRIBUTE, ConstantMovementBehavior.TYPE);
        theResult.put(SPEED_PROPERTY, speed.get().serialize());
        theResult.put(ROTATIONSPEED_PROPERTY, rotationSpeed.get().serialize());
        return theResult;
    }

    @Override
    public void delete() {
        owner.getGameScene().removeBehaviorFrom(owner, this);
    }

    public static ConstantMovementBehaviorTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        ConstantMovementBehaviorTemplate theResult = new ConstantMovementBehaviorTemplate(aEventmanager, aOwner);
        theResult.speed.setQuietly(Speed.deserialize((Map<String, Object>) aSerializedData.get(SPEED_PROPERTY)));
        Map<String, Object> theRotationSpeed = (Map<String, Object>) aSerializedData.get(ROTATIONSPEED_PROPERTY);
        if (theRotationSpeed != null) {
            theResult.rotationSpeed.setQuietly(Speed.deserialize(theRotationSpeed));
        }
        return theResult;
    }
}
