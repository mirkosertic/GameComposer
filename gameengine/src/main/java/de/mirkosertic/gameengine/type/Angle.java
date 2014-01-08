package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;

public class Angle {

    public final int angleInDegrees;

    public Angle(int aAngleInDegrees) {
        angleInDegrees = aAngleInDegrees;
    }

    public Angle invert() {
        return new Angle(-angleInDegrees);
    }

    public float toRadians() {
        return (float) Math.toRadians(angleInDegrees);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Angle angle = (Angle) o;

        if (angleInDegrees != angle.angleInDegrees) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return angleInDegrees;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("angleindegrees", Integer.toString(angleInDegrees));
        return theResult;
    }

    public static Angle deserialize(Map<String, Object> aSerializedData) {
        int theAngleInDegrees = Integer.parseInt((String) aSerializedData.get("angleindegrees"));
        return new Angle(theAngleInDegrees);
    }

    public static Angle fromRadians(float aAngleInRadians) {
        return new Angle((int) Math.toDegrees(aAngleInRadians));
    }
}