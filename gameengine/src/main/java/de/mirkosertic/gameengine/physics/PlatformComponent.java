package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.types.Position;

public class PlatformComponent extends GameComponent {

    static final String TYPE = "PlatformComponent";

    private final GameRuntime gameRuntime;
    private final GameObjectInstance objectInstance;
    private final PlatformComponentTemplate platformTemplate;

    private boolean jumping;
    private boolean leftKeyDown;
    private boolean rightKeyDown;
    private boolean upKeyDown;

    PlatformComponent(GameObjectInstance aObjectInstance, GameRuntime aGameRuntime) {
        gameRuntime = aGameRuntime;
        objectInstance = aObjectInstance;
        platformTemplate = aObjectInstance.getOwnerGameObject().getComponentTemplate(PlatformComponentTemplate.class);
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

    boolean isJumping() {
        return jumping;
    }

    void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    void handleGameLoop(SystemTick aEvent) {
        if (upKeyDown && !isJumping()) {
            gameRuntime.getEventManager().fire(new ApplyImpulseToGameObjectInstance(objectInstance, 0, platformTemplate.jumpImpulseProperty().get()));
            setJumping(true);
        }
        if (rightKeyDown) {
            gameRuntime.getEventManager().fire(new ApplyForceToGameObjectInstance(objectInstance, platformTemplate.leftRightImpulseProperty().get(), 0f));
        }
        if (leftKeyDown) {
            gameRuntime.getEventManager().fire(new ApplyForceToGameObjectInstance(objectInstance, -platformTemplate.leftRightImpulseProperty().get(), 0));
        }
    }

    void handleKeyPressed(KeyPressed aEvent) {
        if (aEvent.keyCodeProperty().get() == platformTemplate.moveLeftKeyProperty().get()) {
            leftKeyDown = true;
        }
        if (aEvent.keyCodeProperty().get() == platformTemplate.moveRightKeyProperty().get()) {
            rightKeyDown = true;
        }
        if (aEvent.keyCodeProperty().get() == platformTemplate.jumpKeyProperty().get()) {
            upKeyDown = true;
        }
    }

    void handleKeyReleased(KeyReleased aEvent) {
        if (aEvent.keyCodeProperty().get() == platformTemplate.moveLeftKeyProperty().get()) {
            leftKeyDown = false;
        }
        if (aEvent.keyCodeProperty().get() == platformTemplate.moveRightKeyProperty().get()) {
            rightKeyDown = false;
        }
        if (aEvent.keyCodeProperty().get() == platformTemplate.jumpKeyProperty().get()) {
            upKeyDown = false;
        }
    }

    void handleCollision(GameObjectCollision aEvent) {
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