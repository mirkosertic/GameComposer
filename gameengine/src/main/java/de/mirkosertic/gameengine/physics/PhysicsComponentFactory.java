package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameComponentFactory;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class PhysicsComponentFactory implements GameComponentFactory<PhysicsComponent> {

    public PhysicsComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new PhysicsComponent(aInstance, aGameRuntime);
    }
}