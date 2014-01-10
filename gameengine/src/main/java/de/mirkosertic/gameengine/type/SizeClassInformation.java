package de.mirkosertic.gameengine.type;

public class SizeClassInformation extends ClassInformation {

    public final static Field<Size, Integer> WIDTH = new Field<Size, Integer>("width", Integer.class) {
        @Override
        public Integer getValue(Size aObject) {
            return aObject.width;
        }
    };

    public final static Field<Size, Integer> HEIGHT = new Field<Size, Integer>("height", Integer.class) {
        @Override
        public Integer getValue(Size aObject) {
            return aObject.height;
        }
    };

    public static final SizeClassInformation INSTANCE = new SizeClassInformation();

    private SizeClassInformation() {
        register(WIDTH);
        register(HEIGHT);
    }
}