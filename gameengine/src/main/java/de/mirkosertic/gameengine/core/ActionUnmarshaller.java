package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface ActionUnmarshaller {

    String getTypeKey();

    Action unmarshall(IORegistry aRegistry, Map<String, Object> aSerializedData);
}
