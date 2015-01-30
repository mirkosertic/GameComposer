package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;

@InheritedClassInformation
public class EnableDynamicPhysics extends GameEvent {

    private static final EnableDynamicPhysicsClassInformation CIINSTANCE = new EnableDynamicPhysicsClassInformation();

    @ReflectiveField
    public final GameObjectInstance object;

    public EnableDynamicPhysics(GameObjectInstance aObject) {
        super("EnableDynamicPhysics");
        object = aObject;
    }

    @Override
    public EnableDynamicPhysicsClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}