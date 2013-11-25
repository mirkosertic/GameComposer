package de.mirkosertic.gameengine.types;

import java.util.HashMap;
import java.util.Map;

public class Position {

    public final float x;
    public final float y;

    public Position() {
        this(0f, 0f);
    }

    public Position(float aX, float aY) {
        x = aX;
        y = aY;
    }

    public Position(double aX, double aY) {
        x = (float) aX;
        y = (float) aY;
    }

    public Map<String, Object> serializeToMap() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("x", Float.toString(x));
        theResult.put("y", Float.toString(y));
        return theResult;
    }

    public static Position deserialize(Map<String, Object> aSerializedStructure) {
        float theX = Float.valueOf((String) aSerializedStructure.get("x"));
        float theY = Float.valueOf((String) aSerializedStructure.get("y"));
        return new Position(theX, theY);
    }
}
