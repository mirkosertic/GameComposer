package de.mirkosertic.gameengine.physics.jbox2d;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.physics.*;

public class JBox2DGamePhysicsManagerFactory implements GamePhysicsManagerFactory {

    public JBox2DGamePhysicsManager create(GameEventManager aEventManager) {
        final JBox2DGamePhysicsManager thePhysicsManager = new JBox2DGamePhysicsManager(aEventManager);
        aEventManager.register(null, GameObjectInstanceAddedToSceneEvent.class, new GameEventListener<GameObjectInstanceAddedToSceneEvent>() {
            public void handleGameEvent(GameObjectInstanceAddedToSceneEvent aEvent) {
                thePhysicsManager.gameObjectInstanceAddedToScene(aEvent.getGameObjectInstance());
            }
        });
        aEventManager.register(null, GameObjectInstanceRemovedFromSceneEvent.class, new GameEventListener<GameObjectInstanceRemovedFromSceneEvent>() {
            public void handleGameEvent(GameObjectInstanceRemovedFromSceneEvent aEvent) {
                thePhysicsManager.gameObjectInstanceRemovedFromScene(aEvent.getGameObjectInstance());
            }
        });
        aEventManager.register(null, ApplyImpulseToGameObjectInstance.class, new GameEventListener<ApplyImpulseToGameObjectInstance>() {
            public void handleGameEvent(ApplyImpulseToGameObjectInstance aEvent) {
                thePhysicsManager.applyImpulse(aEvent.getGameObjectInstance(), aEvent.getImpulseX(), aEvent.getImpulseY());
            }
        });
        aEventManager.register(null, ApplyForceToGameObjectInstance.class, new GameEventListener<ApplyForceToGameObjectInstance>() {
            public void handleGameEvent(ApplyForceToGameObjectInstance aEvent) {
                thePhysicsManager.applyForce(aEvent.getGameObjectInstance(), aEvent.getForceX(), aEvent.getForceY());
            }
        });
        aEventManager.register(null, GameObjectInstancePositionChangedEvent.class, new GameEventListener<GameObjectInstancePositionChangedEvent>() {
            public void handleGameEvent(GameObjectInstancePositionChangedEvent aEvent) {
                thePhysicsManager.gameObjectInstancePositionChanged(aEvent.getGameObjectInstance(), aEvent.getPosition());
            }
        });
        aEventManager.register(null, GameObjectInstanceSizeChangedEvent.class, new GameEventListener<GameObjectInstanceSizeChangedEvent>() {
            public void handleGameEvent(GameObjectInstanceSizeChangedEvent aEvent) {
                thePhysicsManager.gameObjectInstanceSizeChanged(aEvent.getGameObjectInstance(), aEvent.getSize());
            }
        });
        aEventManager.register(null, DisableDynamicPhysicsEvent.class, new GameEventListener<DisableDynamicPhysicsEvent>() {
            @Override
            public void handleGameEvent(DisableDynamicPhysicsEvent aEvent) {
                thePhysicsManager.disableDynamicPhysicsOn(aEvent.getObject());
            }
        });
        aEventManager.register(null, EnableDynamicPhysicsEvent.class, new GameEventListener<EnableDynamicPhysicsEvent>() {
            @Override
            public void handleGameEvent(EnableDynamicPhysicsEvent aEvent) {
                thePhysicsManager.enableDynamicPhysicsOn(aEvent.getObject());
            }
        });
        return thePhysicsManager;
    }
}
