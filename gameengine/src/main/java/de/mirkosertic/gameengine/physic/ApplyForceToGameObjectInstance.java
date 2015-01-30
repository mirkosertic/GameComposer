package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;
import de.mirkosertic.gameengine.type.Force;

@InheritedClassInformation
public class ApplyForceToGameObjectInstance extends GameEvent {

    private static final ApplyForceToGameObjectInstanceClassInformation CIINSTANCE = new ApplyForceToGameObjectInstanceClassInformation();

    @ReflectiveField
    public final GameObjectInstance instance;

    @ReflectiveField
    public final Force force;

    public ApplyForceToGameObjectInstance(GameObjectInstance aInstance, Force aForce) {
        super("ApplyForceToGameObjectInstance");
        instance = aInstance;
        force = aForce;
    }

    @Override
    public ApplyForceToGameObjectInstanceClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
