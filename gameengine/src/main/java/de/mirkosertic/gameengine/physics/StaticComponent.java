package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class StaticComponent extends StaticData implements GameComponent {
    
    public static final String TYPE = "StaticComponent";

    private GameObjectInstance objectInstance;

    StaticComponent(GameObjectInstance aObjectInstance) {
        objectInstance = aObjectInstance;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    public static StaticComponent deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return new StaticComponent(aObjectInstance);
    }
}
