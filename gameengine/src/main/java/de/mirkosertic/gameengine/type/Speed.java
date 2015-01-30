package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

import java.util.HashMap;
import java.util.Map;

public class Speed implements Reflectable {

    private static final SpeedClassInformation CIINSTANCE = new SpeedClassInformation();

    public final long speed;

    public Speed(long aSpeed) {
        speed = aSpeed;
    }

    public Long get() {
        return speed;
    }

    @ReflectiveMethod
    public Speed incrementBy(int aAmount) {
        return new Speed(speed + aAmount);
    }

    @ReflectiveMethod
    public Speed resetToZero() {
        return new Speed(0);
    }

    @Override
    public SpeedClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("speed", Long.toString(speed));
        return theResult;
    }

    public static Speed deserialize(Map<String, Object> aSerializedData) {
        long theSpeed = Long.valueOf((String) aSerializedData.get("speed"));
        return new Speed(theSpeed);
    }
}
