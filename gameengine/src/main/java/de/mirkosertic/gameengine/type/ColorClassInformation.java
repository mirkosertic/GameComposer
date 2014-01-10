package de.mirkosertic.gameengine.type;

public class ColorClassInformation extends ClassInformation {

    public final static Field<Color, Integer> R = new Field<Color, Integer>("r", Integer.class) {
        @Override
        public Integer getValue(Color aObject) {
            return aObject.r;
        }
    };

    public final static Field<Color, Integer> G = new Field<Color, Integer>("g", Integer.class) {
        @Override
        public Integer getValue(Color aObject) {
            return aObject.g;
        }
    };

    public final static Field<Color, Integer> B = new Field<Color, Integer>("b", Integer.class) {
        @Override
        public Integer getValue(Color aObject) {
            return aObject.b;
        }
    };

    public static final ColorClassInformation INSTANCE = new ColorClassInformation();

    private ColorClassInformation() {
        register(R);
        register(G);
        register(B);
    }
}
