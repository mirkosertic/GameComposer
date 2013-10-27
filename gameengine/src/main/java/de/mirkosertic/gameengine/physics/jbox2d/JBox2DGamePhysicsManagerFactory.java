package de.mirkosertic.gameengine.physics.jbox2d;

import de.mirkosertic.gameengine.core.GameEventListener;
import de.mirkosertic.gameengine.core.GameEventManager;
import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToSceneEvent;
import de.mirkosertic.gameengine.physics.ApplyForceToGameObjectInstance;
import de.mirkosertic.gameengine.physics.ApplyImpulseToGameObjectInstance;
import de.mirkosertic.gameengine.physics.PhysicsManagerFactory;

public class JBox2DGamePhysicsManagerFactory implements PhysicsManagerFactory {

    public JBox2DGamePhysicsManager create(GameEventManager aEventManager) {
        final JBox2DGamePhysicsManager thePhysicsManager = new JBox2DGamePhysicsManager(aEventManager);
        aEventManager.register(null, GameObjectInstanceAddedToSceneEvent.class, new GameEventListener<GameObjectInstanceAddedToSceneEvent>() {
            public void handleGameEvent(GameObjectInstanceAddedToSceneEvent aEvent) {
                thePhysicsManager.gameObjectInstanceAddedToScene(aEvent.getGameObjectInstance());
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
        return thePhysicsManager;
    }
}
