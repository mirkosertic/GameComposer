package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;

public class Speed implements ValueProvider<Long> {

    public final long speed;

    public Speed(long aSpeed) {
        speed = aSpeed;
    }

    @Override
    public Long get() {
        return speed;
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
