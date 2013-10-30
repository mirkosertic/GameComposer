package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class StaticComponentTemplate extends StaticData implements GameComponentTemplate<StaticComponent> {

    public StaticComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new StaticComponent(aInstance);
    }

    @Override
    public String getTypeKey() {
        return StaticComponent.TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(StaticComponent.TYPE_ATTRIBUTE, StaticComponent.TYPE);
        return theResult;
    }

    public static StaticComponentTemplate deserialize(Map<String, Object> aSerializedData) {
        return new StaticComponentTemplate();
    }
}
