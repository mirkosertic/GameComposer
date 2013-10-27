package de.mirkosertic.gameengine.sprites;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.ResourceName;

public class SpriteComponent implements GameComponent {

    private GameRuntime gameRuntime;
    private GameObjectInstance objectInstance;

    private ResourceName resourceName;

    SpriteComponent(GameObjectInstance aObjectInstance, GameRuntime aGameRuntime, ResourceName aResourceName) {
        gameRuntime = aGameRuntime;
        objectInstance = aObjectInstance;
        resourceName = aResourceName;
    }
    public ResourceName getResourceName() {
        return resourceName;
    }
}