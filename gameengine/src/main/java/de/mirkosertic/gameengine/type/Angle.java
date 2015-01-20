package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

import java.util.HashMap;
import java.util.Map;

public class Angle implements Reflectable {

    public final int angleInDegrees;

    public Angle(int aAngleInDegrees) {
        while (aAngleInDegrees < 0) {
            aAngleInDegrees += 360;
        }
        angleInDegrees = aAngleInDegrees % 360;
    }

    public Integer get() {
        return angleInDegrees;
    }

    @ReflectiveMethod
    public Angle add(int aDiff) {
        return new Angle(angleInDegrees + aDiff);
    }

    @ReflectiveMethod
    public Angle invert() {
        return new Angle(-angleInDegrees);
    }

    @ReflectiveMethod
    public Angle snapTo(int aStep) {
        int theStepHalf = aStep / 2;
        for (int i = 0;i<=360 / aStep;i++) {
            int theStep = i * aStep;
            int theMinAngle = theStep - theStepHalf;
            int theMaxAngle = theStep + theStepHalf;
            if (angleInDegrees >=theMinAngle && angleInDegrees < theMaxAngle) {
                return new Angle(theStep);
            }
        }
        return new Angle(angleInDegrees);
    }

    @ReflectiveMethod
    public Angle resetToZero() {
        return new Angle(0);
    }

    public float toRadians() {
        return (float) Math.toRadians(angleInDegrees);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Angle angle = (Angle) o;

        return angleInDegrees == angle.angleInDegrees;

    }

    @Override
    public int hashCode() {
        return angleInDegrees;
    }

    @Override
    public AngleClassInformation getClassInformation() {
        return AngleClassInformation.INSTANCE;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
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