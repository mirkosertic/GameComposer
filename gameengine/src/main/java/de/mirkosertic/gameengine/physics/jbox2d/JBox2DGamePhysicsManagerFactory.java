package de.mirkosertic.gameengine.physics.jbox2d;

import de.mirkosertic.gameengine.core.GameLoopRun;
import de.mirkosertic.gameengine.core.GameObjectConfigurationChanged;
import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToScene;
import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromScene;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.physics.*;
import de.mirkosertic.gameengine.physics.DisableDynamicPhysics;

public class JBox2DGamePhysicsManagerFactory implements GamePhysicsManagerFactory {

    public JBox2DGamePhysicsManager create(GameEventManager aEventManager) {
        final JBox2DGamePhysicsManager thePhysicsManager = new JBox2DGamePhysicsManager(aEventManager);
        aEventManager.register(null, GameObjectInstanceAddedToScene.class, new GameEventListener<GameObjectInstanceAddedToScene>() {
            public void handleGameEvent(GameObjectInstanceAddedToScene aEvent) {
                thePhysicsManager.gameObjectInstanceAddedToScene(aEvent.instanceProperty().get());
            }
        });
        aEventManager.register(null, GameObjectInstanceRemovedFromScene.class, new GameEventListener<GameObjectInstanceRemovedFromScene>() {
            public void handleGameEvent(GameObjectInstanceRemovedFromScene aEvent) {
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
        aEventManager.register(null, DisableDynamicPhysics.class, new GameEventListener<DisableDynamicPhysics>() {
            @Override
            public void handleGameEvent(DisableDynamicPhysics aEvent) {
                thePhysicsManager.disableDynamicPhysicsOn(aEvent.objectProperty().get());
            }
        });
        aEventManager.register(null, EnableDynamicPhysics.class, new GameEventListener<EnableDynamicPhysics>() {
            @Override
            public void handleGameEvent(EnableDynamicPhysics aEvent) {
                thePhysicsManager.enableDynamicPhysicsOn(aEvent.objectProperty().get());
            }
        });
        aEventManager.register(null, GameObjectConfigurationChanged.class, new GameEventListener<GameObjectConfigurationChanged>() {
            @Override
            public void handleGameEvent(GameObjectConfigurationChanged aEvent) {
                thePhysicsManager.updateGameObjectConfiguration(aEvent.objectProperty().get());
            }
        });
        aEventManager.register(null, GameLoopRun.class, new GameEventListener<GameLoopRun>() {
            @Override
            public void handleGameEvent(GameLoopRun aEvent) {
                thePhysicsManager.proceedGame(aEvent.gameTimeProperty().get(), aEvent.elapsedTimeSinceLastLoopProperty().get());
            }
        });
        return thePhysicsManager;
    }
}
