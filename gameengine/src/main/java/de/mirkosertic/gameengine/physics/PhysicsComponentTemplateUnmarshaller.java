package de.mirkosertic.gameengine.physics;

import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;

public class PhysicsComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<PhysicsComponentTemplate> {

    @Override
    public String getTypeKey() {
        return PhysicsComponent.TYPE;
    }

    @Override
    public PhysicsComponentTemplate deserialize(GameObject aOwner, Map<String, Object> aSerializedData) {
        return PhysicsComponentTemplate.deserialize(aOwner, aSerializedData);
    }
}
