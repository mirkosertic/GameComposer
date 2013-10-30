package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface GameComponentTemplateUnmarshaller<T extends GameComponentTemplate> {

    String getTypeKey();

    T deserialize(Map<String, Object> aSerializedData);
}
