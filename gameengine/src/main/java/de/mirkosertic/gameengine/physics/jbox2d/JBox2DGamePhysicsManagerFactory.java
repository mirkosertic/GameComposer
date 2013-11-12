package de.mirkosertic.gameengine.physics.jbox2d;

import de.mirkosertic.gameengine.core.GameObjectConfigurationChangedEvent;
import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToSceneEvent;
import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromSceneEvent;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.physics.ApplyForceToGameObjectInstance;
import de.mirkosertic.gameengine.physics.ApplyImpulseToGameObjectInstance;
import de.mirkosertic.gameengine.physics.DisableDynamicPhysicsEvent;
import de.mirkosertic.gameengine.physics.EnableDynamicPhysicsEvent;
import de.mirkosertic.gameengine.physics.GamePhysicsManagerFactory;

public class JBox2DGamePhysicsManagerFactory implements GamePhysicsManagerFactory {

    public JBox2DGamePhysicsManager create(GameEventManager aEventManager) {
        final JBox2DGamePhysicsManager thePhysicsManager = new JBox2DGamePhysicsManager(aEventManager);
        aEventManager.register(null, GameObjectInstanceAddedToSceneEvent.class, new GameEventListener<GameObjectInstanceAddedToSceneEvent>() {
            public void handleGameEvent(GameObjectInstanceAddedToSceneEvent aEvent) {
                thePhysicsManager.gameObjectInstanceAddedToScene(aEvent.instanceProperty().get());
            }
        });
        aEventManager.register(null, GameObjectInstanceRemovedFromSceneEvent.class, new GameEventListener<GameObjectInstanceRemovedFromSceneEvent>() {
            public void handleGameEvent(GameObjectInstanceRemovedFromSceneEvent aEvent) {
                thePhysicsManager.gameObjectInstanceRemovedFromScene(aEvent.instanceProperty().get());
            }
        });
        aEventManager.register(null, ApplyImpulseToGameObjectInstance.class, new GameEventListener<ApplyImpulseToGameObjectInstance>() {
            public void handleGameEvent(ApplyImpulseToGameObjectInstance aEvent) {
                thePhysicsManager.applyImpulse(aEvent.instanceProperty().get(), aEvent.impulseXProperty().get(), aEvent.impulseYProperty().get());
            }
        });
        aEventManager.register(null, ApplyForceToGameObjectInstance.class, new GameEventListener<ApplyForceToGameObjectInstance>() {
            public void handleGameEvent(ApplyForceToGameObjectInstance aEvent) {
                thePhysicsManager.applyForce(aEvent.instanceProperty().get(), aEvent.forceXProperty().get(), aEvent.forceYProperty().get());
            }
        });
        aEventManager.register(null, DisableDynamicPhysicsEvent.class, new GameEventListener<DisableDynamicPhysicsEvent>() {
            @Override
            public void handleGameEvent(DisableDynamicPhysicsEvent aEvent) {
                thePhysicsManager.disableDynamicPhysicsOn(aEvent.objectProperty().get());
            }
        });
        aEventManager.register(null, EnableDynamicPhysicsEvent.class, new GameEventListener<EnableDynamicPhysicsEvent>() {
            @Override
            public void handleGameEvent(EnableDynamicPhysicsEvent aEvent) {
                thePhysicsManager.enableDynamicPhysicsOn(aEvent.objectProperty().get());
            }
        });
        aEventManager.register(null, GameObjectConfigurationChangedEvent.class, new GameEventListener<GameObjectConfigurationChangedEvent>() {
            @Override
            public void handleGameEvent(GameObjectConfigurationChangedEvent aEvent) {
                thePhysicsManager.updateGameObjectConfiguration(aEvent.objectProperty().get());
            }
        });
        return thePhysicsManager;
    }
}
