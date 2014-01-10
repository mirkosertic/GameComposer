package de.mirkosertic.gameengine.type;

public class Force implements Reflectable<ForceClassInformation> {

    public final float forceX;
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
