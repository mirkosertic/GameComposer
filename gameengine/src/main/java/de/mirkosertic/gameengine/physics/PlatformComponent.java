package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.input.KeyPressed;
import de.mirkosertic.gameengine.input.KeyReleased;
import de.mirkosertic.gameengine.types.CollisionPosition;
import de.mirkosertic.gameengine.types.GameKeyCode;
import de.mirkosertic.gameengine.types.Position;

public class PlatformComponent extends GameComponent implements Platform {

    static final String TYPE = "PlatformComponent";

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

    PlatformComponent(GameObjectInstance aObjectInstance, GameRuntime aGameRuntime) {
        gameRuntime = aGameRuntime;
        objectInstance = aObjectInstance;

        PlatformComponentTemplate theTemplate = aObjectInstance.getOwnerGameObject().getComponentTemplate(PlatformComponentTemplate.class);
        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        moveLeftKey = registerProperty(new Property<GameKeyCode>(this, "moveLeftKey", theTemplate.moveLeftKeyProperty().get(), theEventManager));
        moveRightKey = registerProperty(new Property<GameKeyCode>(this, "moveRightKey", theTemplate.moveRightKeyProperty().get(), theEventManager));
        jumpKey = registerProperty(new Property<GameKeyCode>(this, "jumpKey", theTemplate.jumpKeyProperty().get(), theEventManager));
        leftRightImpulse = registerProperty(new Property<Float>(this, "leftRightImpulse", theTemplate.leftRightImpulseProperty().get(), theEventManager));
        jumpImpulse = registerProperty(new Property<Float>(this, "jumpImpulse", theTemplate.jumpImpulseProperty().get(), theEventManager));
    }

    PlatformComponent(GameObjectInstance aObjectInstance, GameRuntime aGameRuntime, PlatformComponentTemplate aTemplate) {
        this(aObjectInstance, aGameRuntime);
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    public void registerEvents(GameRuntime aGameRuntime) {
        aGameRuntime.getEventManager().register(objectInstance, KeyPressed.class, new GameEventListener<KeyPressed>() {
            public void handleGameEvent(KeyPressed aEvent) {
                handleKeyPressed(aEvent);
            }
        });
        aGameRuntime.getEventManager().register(objectInstance, KeyReleased.class, new GameEventListener<KeyReleased>() {
            public void handleGameEvent(KeyReleased aEvent) {
                handleKeyReleased(aEvent);
            }
        });
        aGameRuntime.getEventManager().register(objectInstance, GameObjectCollision.class, new GameEventListener<GameObjectCollision>() {
            public void handleGameEvent(GameObjectCollision aEvent) {
                handleCollision(aEvent);
            }
        });
        aGameRuntime.getEventManager().register(objectInstance, SystemTick.class, new GameEventListener<SystemTick>() {
            public void handleGameEvent(SystemTick aEvent) {
                handleGameLoop(aEvent);
            }
        });

    }

    public Property<GameKeyCode> moveLeftKeyProperty() {
        return moveLeftKey;
    }

    public Property<GameKeyCode> moveRightKeyProperty() {
        return moveRightKey;
    }

    public Property<GameKeyCode> jumpKeyProperty() {
        return jumpKey;
    }

    public Property<Float> leftRightImpulseProperty() {
        return leftRightImpulse;
    }

    public Property<Float> jumpImpulseProperty() {
        return jumpImpulse;
    }

    boolean isJumping() {
        return jumping;
    }

    void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    void handleGameLoop(SystemTick aEvent) {
        if (upKeyDown && !isJumping()) {
            gameRuntime.getEventManager().fire(new ApplyImpulseToGameObjectInstance(objectInstance, 0, jumpImpulse.get()));
            setJumping(true);
        }
        if (rightKeyDown) {
            gameRuntime.getEventManager().fire(new ApplyForceToGameObjectInstance(objectInstance, leftRightImpulse.get(), 0f));
        }
        if (leftKeyDown) {
            gameRuntime.getEventManager().fire(new ApplyForceToGameObjectInstance(objectInstance, -leftRightImpulse.get(), 0));
        }
    }

    void handleKeyPressed(KeyPressed aEvent) {
        if (aEvent.keyCodeProperty().get() == moveLeftKey.get()) {
            leftKeyDown = true;
        }
        if (aEvent.keyCodeProperty().get() == moveRightKey.get()) {
            rightKeyDown = true;
        }
        if (aEvent.keyCodeProperty().get() == jumpKey.get()) {
            upKeyDown = true;
        }
    }

    void handleKeyReleased(KeyReleased aEvent) {
        if (aEvent.keyCodeProperty().get() == moveLeftKey.get()) {
            leftKeyDown = false;
        }
        if (aEvent.keyCodeProperty().get() == moveRightKey.get()) {
            rightKeyDown = false;
        }
        if (aEvent.keyCodeProperty().get() == jumpKey.get()) {
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
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public PlatformComponentTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(PlatformComponentTemplate.class);
    }

    public static PlatformComponent deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData, GameRuntime aGameRuntime) {
        PlatformComponent theComponent = new PlatformComponent(aObjectInstance, aGameRuntime);
        theComponent.registerEvents(aGameRuntime);
        return theComponent;
    }
}