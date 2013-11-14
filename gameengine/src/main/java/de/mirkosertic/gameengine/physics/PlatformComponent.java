package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.types.Position;

public class PlatformComponent implements GameComponent {

    public static final String TYPE = "PlatformComponent";

    private final GameRuntime gameRuntime;
    private final GameObjectInstance objectInstance;
    private final PlatformComponentTemplate platformTemplate;

    private boolean jumping;

    PlatformComponent(GameObjectInstance aObjectInstance, GameRuntime aGameRuntime) {
        gameRuntime = aGameRuntime;
        objectInstance = aObjectInstance;
        platformTemplate = aObjectInstance.getOwnerGameObject().getComponentTemplate(PlatformComponentTemplate.class);
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    public void registerEvents(GameRuntime aGameRuntime) {
        aGameRuntime.getEventManager().register(objectInstance, KeyPressedGameEvent.class, new GameEventListener<KeyPressedGameEvent>() {
            public void handleGameEvent(KeyPressedGameEvent aEvent) {
                handleKeyPressed(aEvent);
            }
        });
        aGameRuntime.getEventManager().register(objectInstance, KeyReleasedGameEvent.class, new GameEventListener<KeyReleasedGameEvent>() {
            public void handleGameEvent(KeyReleasedGameEvent aEvent) {
                handleKeyReleased(aEvent);
            }
        });
        aGameRuntime.getEventManager().register(objectInstance, GameObjectCollisionEvent.class, new GameEventListener<GameObjectCollisionEvent>() {
            public void handleGameEvent(GameObjectCollisionEvent aEvent) {
                handleCollision(aEvent);
            }
        });
    }

    boolean isJumping() {
        return jumping;
    }

    void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    void handleKeyPressed(KeyPressedGameEvent aEvent) {
        if (aEvent.keyCodeProperty().get() == platformTemplate.moveLeftKeyProperty().get()) {
            gameRuntime.getEventManager().fire(new ApplyForceToGameObjectInstance(objectInstance, -platformTemplate.leftRightImpulseProperty().get(), 0));
        }
        if (aEvent.keyCodeProperty().get() == platformTemplate.moveRightKeyProperty().get()) {
            gameRuntime.getEventManager().fire(new ApplyForceToGameObjectInstance(objectInstance, platformTemplate.leftRightImpulseProperty().get(), 0f));
        }
        if (aEvent.keyCodeProperty().get() == platformTemplate.jumpKeyProperty().get() && !isJumping()) {
            gameRuntime.getEventManager().fire(new ApplyImpulseToGameObjectInstance(objectInstance, 0, platformTemplate.jumpImpulseProperty().get()));
            setJumping(true);
        }
    }

    void handleKeyReleased(KeyReleasedGameEvent aEvent) {
        // Reset Acceleration to zero ?
    }

    void handleCollision(GameObjectCollisionEvent aEvent) {
        GameObjectInstance theOtherInstance = aEvent.getOtherInstanceOrNullIfNotAffected(objectInstance);
        if (theOtherInstance != null) {
            Position theOtherPosition = theOtherInstance.positionProperty().get();
            Position thePosition = objectInstance.positionProperty().get();
            double my = theOtherPosition.y - thePosition.y;
            if (my > 0) {
                // Object is under the game object
                setJumping(false);
            } else {
                double mx = theOtherPosition.x - thePosition.x;
                // Object is on the right side
                if (mx > 0) {
                    setJumping(false);
                }
                // Object is on the left side
                if (mx < 0) {
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