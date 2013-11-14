package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public interface GameComponentTemplateUnmarshaller<T extends GameComponentTemplate> {

    String getTypeKey();

    T deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData);
}
