package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public interface BehaviorTemplateUnmarshaller<T extends BehaviorTemplate> {

    String getTypeKey();

    T deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData);
}
