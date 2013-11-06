package de.mirkosertic.gameengine.physics;

import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplateUnmarshaller;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameRuntime;

public class StaticComponentTemplateUnmarshaller implements GameComponentTemplateUnmarshaller<StaticComponentTemplate> {

    @Override
    public String getTypeKey() {
        return StaticComponent.TYPE;
    }

    @Override
    public StaticComponentTemplate deserialize(GameObject aOwner, Map<String, Object> aSerializedData) {
        return StaticComponentTemplate.deserialize(aOwner, aSerializedData);
    }
}
