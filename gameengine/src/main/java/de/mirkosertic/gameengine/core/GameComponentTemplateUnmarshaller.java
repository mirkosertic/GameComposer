package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface GameComponentTemplateUnmarshaller<T extends GameComponentTemplate> {

    String getTypeKey();

    T deserialize(GameObject aOwner, Map<String, Object> aSerializedData);
}
