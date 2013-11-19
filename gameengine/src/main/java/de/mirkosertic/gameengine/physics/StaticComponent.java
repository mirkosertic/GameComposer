package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

public class StaticComponent extends GameComponent {
    
    static final String TYPE = "StaticComponent";

    private final GameObjectInstance objectInstance;

    StaticComponent(GameObjectInstance aObjectInstance) {
        objectInstance = aObjectInstance;
    }

    StaticComponent(GameObjectInstance aObjectInstance, StaticComponentTemplate aTemplate) {
        this(aObjectInstance);
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public StaticComponentTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(StaticComponentTemplate.class);
    }

    public static StaticComponent deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return new StaticComponent(aObjectInstance);
    }
}
