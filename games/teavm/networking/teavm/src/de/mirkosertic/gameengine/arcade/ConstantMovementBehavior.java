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

import de.mirkosertic.gameengine.action.SystemTick;
import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.Speed;

import java.util.HashMap;
import java.util.Map;

public class ConstantMovementBehavior implements Behavior, ConstantMovement, Reflectable<ConstantMovementClassInformation> {

    private static final ConstantMovementClassInformation CIINSTANCE = new ConstantMovementClassInformation();

    public static final BehaviorType TYPE = new BehaviorType("ConstantMovement");

    private final GameObjectInstance objectInstance;

    private final Property<Speed> speed;
    private final Property<Speed> rotationSpeed;

    private boolean remoteObject;

    ConstantMovementBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, (ConstantMovementBehaviorTemplate) aObjectInstance.getOwnerGameObject().getBehaviorTemplate(ConstantMovementBehaviorTemplate.TYPE));
    }

    ConstantMovementBehavior(GameObjectInstance aObjectInstance, ConstantMovementBehaviorTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        speed = new Property<>(Speed.class, this, SPEED_PROPERTY, aTemplate.speedProperty().get(), theEventManager);
        rotationSpeed = new Property<>(Speed.class, this, ROTATIONSPEED_PROPERTY, aTemplate.rotationSpeedProperty().get(), theEventManager);
    }

    @Override
    public BehaviorType getType() {
        return TYPE;
    }

    public void registerEvents(GameRuntime aGameRuntime) {
        aGameRuntime.getEventManager().register(objectInstance, SystemTick.TYPE, new GameEventListener<SystemTick>() {
            @Override
            public void handleGameEvent(SystemTick aEvent) {
                if (!remoteObject) {
                    handleGameLoop(aEvent);
                }
            }
        });
    }

    @Override
    public void markAsRemoteObject() {
        remoteObject = true;
    }

    private void handleGameLoop(SystemTick aTick) {

        Position theCurrentPosition = objectInstance.positionProperty().get();
        objectInstance.positionProperty().set(theCurrentPosition.translate(objectInstance.rotationAngleProperty().get(), speed.get().speed / 1000d * aTick.elapsedTimeSinceLastLoop));

        int theAngleDif = (int) (rotationSpeed.get().speed / 1000d * aTick.elapsedTimeSinceLastLoop);
        if (theAngleDif != 0) {
            objectInstance.rotationAngleProperty().set(objectInstance.rotationAngleProperty().get().add(theAngleDif));
        }
    }


    @Override
    public ConstantMovementClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public ConstantMovementBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getBehaviorTemplate(ConstantMovementBehaviorTemplate.TYPE);
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
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        theResult.put(SPEED_PROPERTY, speed.get().serialize());
        theResult.put(ROTATIONSPEED_PROPERTY, rotationSpeed.get().serialize());
        return theResult;
    }

    @Override
    public void delete() {
        objectInstance.getOwnerGameObject().getGameScene().removeBehaviorFrom(objectInstance.getOwnerGameObject(), this);
    }

    @Override
    public GameObjectInstance getInstance() {
        return objectInstance;
    }

    public static ConstantMovementBehavior deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        ConstantMovementBehavior theResult = new ConstantMovementBehavior(aObjectInstance);
        theResult.speed.setQuietly(Speed.deserialize((Map<String, Object>) aSerializedData.get(SPEED_PROPERTY)));
        Map<String, Object> theRotationSpeed = (Map<String, Object>) aSerializedData.get(ROTATIONSPEED_PROPERTY);
        if (theRotationSpeed != null) {
            theResult.rotationSpeed.setQuietly(Speed.deserialize(theRotationSpeed));
        }
        theResult.registerEvents(aRuntime);
        return theResult;
    }
}
