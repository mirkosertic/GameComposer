package de.mirkosertic.gameengine.core;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameScene {

    private Set<GameObjectInstance> instances;
    private GameRuntime gameRuntime;

    public GameScene(GameRuntime aGameRuntime) {
        instances = new HashSet<GameObjectInstance>();
        gameRuntime = aGameRuntime;
    }

    public void addGameObjectInstance(GameObjectInstance aInstance) {
        instances.add(aInstance);
        gameRuntime.getEventManager().fire(new GameObjectInstanceAddedToSceneEvent(aInstance));
    }

    public Set<GameObjectInstance> getInstances() {
        return Collections.unmodifiableSet(instances);
    }
}
