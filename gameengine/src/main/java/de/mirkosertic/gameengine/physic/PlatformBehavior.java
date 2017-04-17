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

import de.mirkosertic.gameengine.action.SystemTick;
import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.input.KeyPressed;
import de.mirkosertic.gameengine.input.KeyReleased;
import de.mirkosertic.gameengine.type.CollisionPosition;
import de.mirkosertic.gameengine.type.Force;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.HashMap;
import java.util.Map;

public class PlatformBehavior implements Behavior, Platform, Reflectable<PlatformClassInformation> {

    private static final PlatformClassInformation CIINSTANCE = new PlatformClassInformation();

    public static final BehaviorType TYPE = new BehaviorType("Platform");

    private final GameRuntime gameRuntime;
    private final GameObjectInstance objectInstance;

    private final Property<GameKeyCode> moveLeftKey;
    private final Property<GameKeyCode> moveRightKey;
    private final Property<GameKeyCode> jumpKey;
    private final Property<Float> leftRightImpulse;
    private final Property<Float> jumpImpulse;

    private boolean jumping;
    private boolean leftKeyDown;
    private boolean rightKeyDown;
    private boolean upKeyDown;

    private boolean remoteObject;

    PlatformBehavior(GameObjectInstance aObjectInstance, GameRuntime aGameRuntime) {
        gameRuntime = aGameRuntime;
        objectInstance = aObjectInstance;

        PlatformBehaviorTemplate theTemplate = aObjectInstance.getOwnerGameObject().getBehaviorTemplate(PlatformBehaviorTemplate.TYPE);
        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        moveLeftKey = new Property<>(GameKeyCode.class, this, MOVE_LEFT_KEY_PROPERTY, theTemplate.moveLeftKeyProperty().get(), theEventManager);
        moveRightKey = new Property<>(GameKeyCode.class, this, MOVE_RIGHT_KEY_PROPERTY, theTemplate.moveRightKeyProperty().get(), theEventManager);
        jumpKey = new Property<>(GameKeyCode.class, this, JUMP_KEY_PROPERTY, theTemplate.jumpKeyProperty().get(), theEventManager);
        leftRightImpulse = new Property<>(Float.class, this, LEFT_RIGHT_IMPULSE_PROPERTY, theTemplate.leftRightImpulseProperty().get(), theEventManager);
        jumpImpulse = new Property<>(Float.class, this, JUMP_IMPULSE_PROPERTY, theTemplate.jumpImpulseProperty().get(), theEventManager);
    }

    @Override
    public PlatformClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public BehaviorType getType() {
        return TYPE;
    }

    public void registerEvents(GameRuntime aGameRuntime) {
        aGameRuntime.getEventManager().register(objectInstance, KeyPressed.TYPE, new GameEventListener<KeyPressed>() {
            @Override
            public void handleGameEvent(KeyPressed aEvent) {
                if (!remoteObject) {
                    handleKeyPressed(aEvent);
                }
            }
        });
        aGameRuntime.getEventManager().register(objectInstance, KeyReleased.TYPE, new GameEventListener<KeyReleased>() {
            @Override
            public void handleGameEvent(KeyReleased aEvent) {
                if (!remoteObject) {
                    handleKeyReleased(aEvent);
                }
            }
        });
        aGameRuntime.getEventManager().register(objectInstance, GameObjectCollision.TYPE, new GameEventListener<GameObjectCollision>() {
            @Override
            public void handleGameEvent(GameObjectCollision aEvent) {
                if (!remoteObject) {
                    handleCollision(aEvent);
                }
            }
        });
        aGameRuntime.getEventManager().register(objectInstance, SystemTick.TYPE, new GameEventListener<SystemTick>() {
            @Override
            public void handleGameEvent(SystemTick aEvent) {
                if (!remoteObject) {
                    handleGameLoop();
                }
            }
        });
    }

    @Override
    public Property<GameKeyCode> moveLeftKeyProperty() {
        return moveLeftKey;
    }

    @Override
    public Property<GameKeyCode> moveRightKeyProperty() {
        return moveRightKey;
    }

    @Override
    public Property<GameKeyCode> jumpKeyProperty() {
        return jumpKey;
    }

    @Override
    public Property<Float> leftRightImpulseProperty() {
        return leftRightImpulse;
    }

    @Override
    public Property<Float> jumpImpulseProperty() {
        return jumpImpulse;
    }

    boolean isJumping() {
        return jumping;
    }

    void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    void handleGameLoop() {
        if (upKeyDown && !isJumping()) {
            gameRuntime.getEventManager().fire(new ApplyImpulseToGameObjectInstance(objectInstance, new Force(0, jumpImpulse.get())));
            setJumping(true);
        }
        if (rightKeyDown) {
            gameRuntime.getEventManager().fire(new ApplyForceToGameObjectInstance(objectInstance, new Force(leftRightImpulse.get(), 0f)));
        }
        if (leftKeyDown) {
            gameRuntime.getEventManager().fire(new ApplyForceToGameObjectInstance(objectInstance, new Force(-leftRightImpulse.get(), 0)));
        }
    }

    void handleKeyPressed(KeyPressed aEvent) {
        if (aEvent.keyCode == moveLeftKey.get()) {
            leftKeyDown = true;
        }
        if (aEvent.keyCode == moveRightKey.get()) {
            rightKeyDown = true;
        }
        if (aEvent.keyCode == jumpKey.get()) {
            upKeyDown = true;
        }
    }

    void handleKeyReleased(KeyReleased aEvent) {
        if (aEvent.keyCode == moveLeftKey.get()) {
            leftKeyDown = false;
        }
        if (aEvent.keyCode == moveRightKey.get()) {
            rightKeyDown = false;
        }
        if (aEvent.keyCode == jumpKey.get()) {
            upKeyDown = false;
        }
    }

    void handleCollision(GameObjectCollision aEvent) {
        GameObjectInstance theOtherInstance = aEvent.getOtherInstanceOrNullIfNotAffected(objectInstance);
        if (theOtherInstance != null) {
            Position theOtherPosition = theOtherInstance.positionProperty().get();
            Position thePosition = objectInstance.positionProperty().get();
            if (CollisionPosition.BOTTOM.detect(thePosition, theOtherPosition)) {
                // Object is under the game object
                setJumping(false);
            } else {
                // Object is on the right side
                if (CollisionPosition.RIGHT.detect(thePosition, theOtherPosition)) {
                    setJumping(false);
                }
                // Object is on the left side
                if (CollisionPosition.LEFT.detect(thePosition, theOtherPosition)) {
                    setJumping(false);
                }
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public PlatformBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getBehaviorTemplate(PlatformBehaviorTemplate.TYPE);
    }

    @Override
    public void delete() {
        objectInstance.getOwnerGameObject().getGameScene().removeBehaviorFrom(objectInstance.getOwnerGameObject(), this);
    }

    @Override
    public GameObjectInstance getInstance() {
        return objectInstance;
    }

    @Override
    public void markAsRemoteObject() {
        remoteObject = true;
    }

    public static PlatformBehavior deserialize(GameObjectInstance aObjectInstance, GameRuntime aGameRuntime) {
        PlatformBehavior theComponent = new PlatformBehavior(aObjectInstance, aGameRuntime);
        theComponent.registerEvents(aGameRuntime);
        return theComponent;
    }
}