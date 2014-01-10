package de.mirkosertic.gameengine.type;

public class ForceClassInformation extends ClassInformation {

    public final static Field<Force, Float> FORCEX = new Field<Force, Float>("forceX", Float.class) {
        @Override
        public Float getValue(Force aObject) {
            return aObject.forceX;
        }
    };

    public final static Field<Force, Float> FORCEY = new Field<Force, Float>("forceY", Float.class) {
        @Override
        public Float getValue(Force aObject) {
            return aObject.forceY;
        }
    };

    public static final ForceClassInformation INSTANCE = new ForceClassInformation();

    private ForceClassInformation() {
        register(FORCEX);
        register(FORCEY);
    }
}
