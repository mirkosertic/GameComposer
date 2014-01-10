package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.SystemTick;
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

    static final String TYPE = "Platform";

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

    PlatformBehavior(GameObjectInstance aObjectInstance, GameRuntime aGameRuntime) {
        gameRuntime = aGameRuntime;
        objectInstance = aObjectInstance;

        PlatformBehaviorTemplate theTemplate = aObjectInstance.getOwnerGameObject().getComponentTemplate(PlatformBehaviorTemplate.class);
        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        moveLeftKey = new Property<GameKeyCode>(GameKeyCode.class, this, MOVE_LEFT_KEY_PROPERTY, theTemplate.moveLeftKeyProperty().get(), theEventManager);
        moveRightKey = new Property<GameKeyCode>(GameKeyCode.class, this, MOVE_RIGHT_KEY_PROPERTY, theTemplate.moveRightKeyProperty().get(), theEventManager);
        jumpKey = new Property<GameKeyCode>(GameKeyCode.class, this, JUMP_KEY_PROPERTY, theTemplate.jumpKeyProperty().get(), theEventManager);
        leftRightImpulse = new Property<Float>(Float.class, this, LEFT_RIGHT_IMPULSE_PROPERTY, theTemplate.leftRightImpulseProperty().get(), theEventManager);
        jumpImpulse = new Property<Float>(Float.class, this, JUMP_IMPULSE_PROPERTY, theTemplate.jumpImpulseProperty().get(), theEventManager);
    }

    @Override
    public PlatformClassInformation getClassInformation() {
        return PlatformClassInformation.INSTANCE;
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
                handleGameLoop();
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
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public PlatformBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(PlatformBehaviorTemplate.class);
    }

    public static PlatformBehavior deserialize(GameObjectInstance aObjectInstance, GameRuntime aGameRuntime) {
        PlatformBehavior theComponent = new PlatformBehavior(aObjectInstance, aGameRuntime);
        theComponent.registerEvents(aGameRuntime);
        return theComponent;
    }
}