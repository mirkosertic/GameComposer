package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameEventListener;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.KeyPressedGameEvent;
import de.mirkosertic.gameengine.core.KeyReleasedGameEvent;

public class PlatformComponentTemplate implements GameComponentTemplate<PlatformComponent> {

    public PlatformComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        final PlatformComponent theSpriteComponent = new PlatformComponent(aInstance, aGameRuntime);
        aGameRuntime.getEventManager().register(aInstance, KeyPressedGameEvent.class, new GameEventListener<KeyPressedGameEvent>() {
            public void handleGameEvent(KeyPressedGameEvent aEvent) {
                theSpriteComponent.handleKeyPressed(aEvent);
            }
        });
        aGameRuntime.getEventManager().register(aInstance, KeyReleasedGameEvent.class, new GameEventListener<KeyReleasedGameEvent>() {
            public void handleGameEvent(KeyReleasedGameEvent aEvent) {
                theSpriteComponent.handleKeyReleased(aEvent);
            }
        });
        aGameRuntime.getEventManager().register(aInstance, GameObjectCollisionEvent.class, new GameEventListener<GameObjectCollisionEvent>() {
            public void handleGameEvent(GameObjectCollisionEvent aEvent) {
                theSpriteComponent.handleCollision(aEvent);
            }
        });
        return theSpriteComponent;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(PlatformComponent.TYPE_ATTRIBUTE, PlatformComponent.TYPE);
        return theResult;
    }
}
