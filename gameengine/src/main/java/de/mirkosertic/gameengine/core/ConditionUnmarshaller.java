package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface ConditionUnmarshaller {

    String getTypeKey();

    Condition unmarshall(Map<String, Object> aSerializedData);
}
