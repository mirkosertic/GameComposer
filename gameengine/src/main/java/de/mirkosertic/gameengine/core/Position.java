package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

public class Position {

    private float x;
    private float y;

    public Position() {
    }

    public Position(float aX, float aY) {
        x = aX;
        y = aY;
    }

    public Position(double aX, double aY) {
        x = (float) aX;
        y = (float) aY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Map<String, Object> serializeToMap() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("x", "" + x);
        theResult.put("y", ""+ y);
        return theResult;
    }

    public static Position deserialize(Map<String, Object> aSerializedStructure) {
        float theX = Float.valueOf((String) aSerializedStructure.get("x"));
        float theY = Float.valueOf((String) aSerializedStructure.get("y"));
        return new Position(theX, theY);
    }
}
