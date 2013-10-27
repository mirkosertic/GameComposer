package de.mirkosertic.gameengine.sprites;

import de.mirkosertic.gameengine.core.GameComponentFactory;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.ResourceName;

public class SpriteComponentFactory implements GameComponentFactory<SpriteComponent> {

    private ResourceName resourceName;

    public SpriteComponentFactory() {
    }

    public ResourceName getResourceName() {
        return resourceName;
    }

    public void setResourceName(ResourceName resourceName) {
        this.resourceName = resourceName;
    }

    public SpriteComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new SpriteComponent(aInstance, aGameRuntime, resourceName);
    }
}
