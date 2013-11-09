package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.types.Angle;
import de.mirkosertic.gameengine.types.Color;
import de.mirkosertic.gameengine.types.Position;
import de.mirkosertic.gameengine.types.Size;

public class PropertyFactory {

    public static Property<String> createStringProperty(Object aOwner, String aName, GameEventListener aEventListener) {
        return new StringProperty(aOwner, aName, aEventListener);
    }

    public static Property<String> createStringProperty(Object aOwner, String aName, String aDefaultValue, GameEventListener aEventListener) {
        return new StringProperty(aOwner, aName, aDefaultValue, aEventListener);
    }

    public static Property<Integer> createIntegerProperty(Object aOwner, String aName, GameEventListener aEventListener) {
        return new IntegerProperty(aOwner, aName, aEventListener);
    }

    public static Property<Boolean> createBooleanProperty(Object aOwner, String aName, GameEventListener aEventListener) {
        return new BooleanProperty(aOwner, aName, aEventListener);
    }

    public static Property<Angle> createAngleProperty(Object aOwner, String aName, GameEventListener aEventListener) {
        return new AngleProperty(aOwner, aName, aEventListener);
    }

    public static Property<Color> createColorProperty(Object aOwner, String aName, GameEventListener aEventListener) {
        return new ColorProperty(aOwner, aName, aEventListener);
    }

    public static Property<Position> createPositionProperty(Object aOwner, String aName, GameEventListener aEventListener) {
        return new PositionProperty(aOwner, aName, aEventListener);
    }

    public static Property<Position> createPositionProperty(Object aOwner, String aName, Position aDefaultValue, GameEventListener aEventListener) {
        return new PositionProperty(aOwner, aName, aDefaultValue, aEventListener);
    }

    public static Property<Size> createSizeProperty(Object aOwner, String aName, GameEventListener aEventListener) {
        return new SizeProperty(aOwner, aName, aEventListener);
    }

    public static Property<Size> createSizeProperty(Object aOwner, String aName, Size aDefaultValue, GameEventListener aEventListener) {
        return new SizeProperty(aOwner, aName, aDefaultValue, aEventListener);
    }

}
