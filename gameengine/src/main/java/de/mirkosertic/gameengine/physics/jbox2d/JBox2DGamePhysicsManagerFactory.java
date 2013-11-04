package de.mirkosertic.gameengine.physics.jbox2d;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.physics.ApplyForceToGameObjectInstance;
import de.mirkosertic.gameengine.physics.ApplyImpulseToGameObjectInstance;
import de.mirkosertic.gameengine.physics.GamePhysicsManagerFactory;

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
        return thePhysicsManager;
    }
}
