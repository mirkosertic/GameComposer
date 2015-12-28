package de.mirkosertic.gameengine.physics.jbox2d;

import de.mirkosertic.gameengine.core.GameObjectConfigurationChanged;
import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToScene;
import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromScene;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.physic.ApplyForceToGameObjectInstance;
import de.mirkosertic.gameengine.physic.ApplyImpulseToGameObjectInstance;
import de.mirkosertic.gameengine.physic.DisableDynamicPhysics;
import de.mirkosertic.gameengine.physic.EnableDynamicPhysics;
import de.mirkosertic.gameengine.physic.GamePhysicsManagerFactory;

public class JBox2DGamePhysicsManagerFactory implements GamePhysicsManagerFactory {

    @Override
    public JBox2DGamePhysicsManager create(GameEventManager aEventManager) {
        final JBox2DGamePhysicsManager thePhysicsManager = new JBox2DGamePhysicsManager(aEventManager);
        aEventManager.register(null, GameObjectInstanceAddedToScene.class, new GameEventListener<GameObjectInstanceAddedToScene>() {
            @Override
            public void handleGameEvent(GameObjectInstanceAddedToScene aEvent) {
                thePhysicsManager.gameObjectInstanceAddedToScene(aEvent.instance);
            }
        });
        aEventManager.register(null, GameObjectInstanceRemovedFromScene.class, new GameEventListener<GameObjectInstanceRemovedFromScene>() {
            @Override
            public void handleGameEvent(GameObjectInstanceRemovedFromScene aEvent) {
                thePhysicsManager.gameObjectInstanceRemovedFromScene(aEvent.instance);
            }
        });
        aEventManager.register(null, ApplyImpulseToGameObjectInstance.class, new GameEventListener<ApplyImpulseToGameObjectInstance>() {
            @Override
            public void handleGameEvent(ApplyImpulseToGameObjectInstance aEvent) {
                thePhysicsManager.applyImpulse(aEvent.instance, aEvent.force);
            }
        });
        aEventManager.register(null, ApplyForceToGameObjectInstance.class, new GameEventListener<ApplyForceToGameObjectInstance>() {
            @Override
            public void handleGameEvent(ApplyForceToGameObjectInstance aEvent) {
                thePhysicsManager.applyForce(aEvent.instance, aEvent.force);
            }
        });
        aEventManager.register(null, DisableDynamicPhysics.class, new GameEventListener<DisableDynamicPhysics>() {
            @Override
            public void handleGameEvent(DisableDynamicPhysics aEvent) {
                thePhysicsManager.disableDynamicPhysicsOn(aEvent.object);
            }
        });
        aEventManager.register(null, EnableDynamicPhysics.class, new GameEventListener<EnableDynamicPhysics>() {
            @Override
            public void handleGameEvent(EnableDynamicPhysics aEvent) {
                thePhysicsManager.enableDynamicPhysicsOn(aEvent.object);
            }
        });
        aEventManager.register(null, GameObjectConfigurationChanged.class, new GameEventListener<GameObjectConfigurationChanged>() {
            @Override
            public void handleGameEvent(GameObjectConfigurationChanged aEvent) {
                thePhysicsManager.updateGameObjectConfiguration(aEvent.object);
            }
        });
        return thePhysicsManager;
    }
}
