package de.mirkosertic.gameengine.physic;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.type.Reflectable;

public class StaticComponent extends GameComponent implements Static, Reflectable<StaticClassInformation> {
    
    static final String TYPE = "StaticComponent";

    private final GameObjectInstance objectInstance;

    StaticComponent(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getComponentTemplate(StaticComponentTemplate.class));
    }

    StaticComponent(GameObjectInstance aObjectInstance, StaticComponentTemplate aTemplate) {
        objectInstance = aObjectInstance;
    }

    @Override
    public StaticClassInformation getClassInformation() {
        return StaticClassInformation.INSTANCE;
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

    public static StaticComponent deserialize(GameObjectInstance aObjectInstance) {
        return new StaticComponent(aObjectInstance);
    }
}
