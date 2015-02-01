package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;

@InheritedClassInformation
public class DisableDynamicPhysics extends GameEvent {

    private static final DisableDynamicPhysicsClassInformation CIINSTANCE = new DisableDynamicPhysicsClassInformation();

    @ReflectiveField
    public final GameObjectInstance object;

    public DisableDynamicPhysics(GameObjectInstance aObject) {
        super("DisableDynamicPhysics");
        object = aObject;
    }

    @Override
    public DisableDynamicPhysicsClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
