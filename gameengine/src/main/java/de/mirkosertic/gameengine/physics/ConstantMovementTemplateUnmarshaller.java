package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public class ConstantMovementTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<ConstantMovementTemplate> {

    @Override
    public String getTypeKey() {
        return ConstantMovement.TYPE;
    }

    @Override
    public ConstantMovementTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return ConstantMovementTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
