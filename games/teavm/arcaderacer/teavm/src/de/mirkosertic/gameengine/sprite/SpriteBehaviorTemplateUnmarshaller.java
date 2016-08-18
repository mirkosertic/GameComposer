package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.core.BehaviorTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.event.GameEventManager;

import java.util.Map;

public class SpriteBehaviorTemplateUnmarshaller implements BehaviorTemplateUnmarshaller<SpriteBehaviorTemplate> {

    @Override
    public String getTypeKey() {
        return SpriteBehavior.TYPE;
    }

    @Override
    public SpriteBehaviorTemplate deserialize(GameEventManager aEventmanager, GameObject aOwner, Map<String, Object> aSerializedData) {
        return SpriteBehaviorTemplate.deserialize(aEventmanager, aOwner, aSerializedData);
    }
}
