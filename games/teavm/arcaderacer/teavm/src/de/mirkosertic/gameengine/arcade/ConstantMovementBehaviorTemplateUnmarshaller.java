package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.core.BehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public class ConstantMovementBehaviorTemplateUnmarshaller implements BehaviorTemplateUnmarshaller<ConstantMovementBehaviorTemplate> {

    @Override
    public String getTypeKey() {
        return ConstantMovementBehavior.TYPE;
    }

    @Override
    public ConstantMovementBehaviorTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return ConstantMovementBehaviorTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
