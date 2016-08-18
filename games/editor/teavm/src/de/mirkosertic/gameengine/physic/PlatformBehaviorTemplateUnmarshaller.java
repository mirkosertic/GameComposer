package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.BehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public class PlatformBehaviorTemplateUnmarshaller implements BehaviorTemplateUnmarshaller<PlatformBehaviorTemplate> {

    @Override
    public String getTypeKey() {
        return PlatformBehavior.TYPE;
    }

    @Override
    public PlatformBehaviorTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return PlatformBehaviorTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
