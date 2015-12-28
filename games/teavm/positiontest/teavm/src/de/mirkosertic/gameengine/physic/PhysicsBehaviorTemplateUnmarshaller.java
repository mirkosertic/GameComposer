package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.BehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public class PhysicsBehaviorTemplateUnmarshaller implements BehaviorTemplateUnmarshaller<PhysicsBehaviorTemplate> {

    @Override
    public String getTypeKey() {
        return PhysicsBehavior.TYPE;
    }

    @Override
    public PhysicsBehaviorTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return PhysicsBehaviorTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
