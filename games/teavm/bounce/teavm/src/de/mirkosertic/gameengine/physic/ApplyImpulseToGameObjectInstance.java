package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.type.Force;

@InheritedClassInformation
public class ApplyImpulseToGameObjectInstance extends GameEvent {

    private static final ApplyImpulseToGameObjectInstanceClassInformation CIINSTANCE = new ApplyImpulseToGameObjectInstanceClassInformation();

    @ReflectiveField
    public final GameObjectInstance instance;

    @ReflectiveField
    public final Force force;

    public ApplyImpulseToGameObjectInstance(GameObjectInstance aInstance, Force aForce) {
        super("ApplyImpulseToGameObjectInstance");
        instance = aInstance;
        force = aForce;
    }

    @Override
    public ApplyImpulseToGameObjectInstanceClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}