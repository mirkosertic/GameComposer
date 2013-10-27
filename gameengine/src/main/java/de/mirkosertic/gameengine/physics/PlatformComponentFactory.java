package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.*;

public class PlatformComponentFactory implements GameComponentFactory<PlatformComponent> {

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
}