package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class PhysicsComponent implements GameComponent {

    private GameRuntime gameRuntime;
    private GameObjectInstance objectInstance;

    PhysicsComponent(GameObjectInstance aObjectInstance, GameRuntime aGameRuntime) {
        gameRuntime = aGameRuntime;
        objectInstance = aObjectInstance;
    }
}
