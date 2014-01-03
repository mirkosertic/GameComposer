package de.mirkosertic.gameengine.physic;

import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class StaticComponentUnmarshaller implements GameComponentUnmarshaller<StaticComponent> {

    @Override
    public String getTypeKey() {
        return StaticComponent.TYPE;
    }

    @Override
    public StaticComponent deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return StaticComponent.deserialize(aObjectInstance);
    }
}
