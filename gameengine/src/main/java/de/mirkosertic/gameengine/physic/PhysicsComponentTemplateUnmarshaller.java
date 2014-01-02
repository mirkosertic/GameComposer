package de.mirkosertic.gameengine.physic;

import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

public class PhysicsComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<PhysicsComponentTemplate> {

    @Override
    public String getTypeKey() {
        return PhysicsComponent.TYPE;
    }

    @Override
    public PhysicsComponentTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return PhysicsComponentTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
