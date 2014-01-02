package de.mirkosertic.gameengine.types;

import java.util.HashMap;
import java.util.Map;

public class Rectangle {

    public final Position position;
    public final Size size;

    public Rectangle() {
        this(new Position(), new Size(Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    public Rectangle(Position aPosition, Size aSize) {
        position = aPosition;
        size = aSize;
    }

    public boolean contains(Position aPosition) {
        return aPosition.x >= position.x &&
                aPosition.y >= position.y &&
                aPosition.x <= position.x + size.width &&
                aPosition.y <= position.y + size.height;
    }

    public Rectangle newPosition(Position aNewPosition) {
        return new Rectangle(aNewPosition, size);
    }

    public Rectangle newSize(Size aNewSize) {
        return new Rectangle(position, aNewSize);
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