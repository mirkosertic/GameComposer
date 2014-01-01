package de.mirkosertic.gameengine.types;

import java.util.HashMap;
import java.util.Map;

public class Rectangle {

    public final Position position;
    public final Size size;

    public Rectangle() {
        this(new Position(), new Size());
    }

    public Rectangle(Position aPosition, Size aSize) {
        position = aPosition;
        size = aSize;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("position", position.serialize());
        theResult.put("size", size.serialize());
        return theResult;
    }

    public static Rectangle deserialize(Map<String, Object> aSerializedData) {
        Position thePosition = Position.deserialize((Map<String, Object>) aSerializedData.get("position"));
        Size theSize = Size.deserialize((Map<String, Object>) aSerializedData.get("size"));
        return new Rectangle(thePosition, theSize);
    }
}
