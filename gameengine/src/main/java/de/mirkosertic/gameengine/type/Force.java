package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.annotations.ReflectiveField;

public class Force implements Reflectable<ForceClassInformation> {

    @ReflectiveField
    public final float forceX;

    @ReflectiveField
    public final float forceY;

    public Force(float aForceX, float aForceY) {
        forceX = aForceX;
        forceY = aForceY;
    }

    @Override
    public ForceClassInformation getClassInformation() {
        return ForceClassInformation.INSTANCE;
    }
}
