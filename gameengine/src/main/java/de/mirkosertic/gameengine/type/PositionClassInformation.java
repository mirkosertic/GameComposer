package de.mirkosertic.gameengine.type;

public class PositionClassInformation extends ClassInformation {

    public final static Field<Position, Float> X = new Field<Position, Float>("x", Float.class) {
        @Override
        public Float getValue(Position aObject) {
            return aObject.x;
        }
    };

    public final static Field<Position, Float> Y = new Field<Position, Float>("y", Float.class) {
        @Override
        public Float getValue(Position aObject) {
            return aObject.y;
        }
    };

    public static final PositionClassInformation INSTANCE = new PositionClassInformation();

    private PositionClassInformation() {
        register(X);
        register(Y);
    }
}
