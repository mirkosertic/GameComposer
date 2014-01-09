package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.HashMap;
import java.util.Map;

public class StaticComponentTemplate extends GameComponentTemplate<StaticComponent> implements Static, Reflectable<StaticClassInformation> {

    private final GameObject owner;

    @UsedByReflection
    public StaticComponentTemplate(GameEventManager aEventManager, GameObject aOwner) {
        owner = aOwner;
    }

    @Override
    public StaticClassInformation getClassInformation() {
        return StaticClassInformation.INSTANCE;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public StaticComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new StaticComponent(aInstance, this);
    }

    @Override
    public String getTypeKey() {
        return StaticComponent.TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(StaticComponent.TYPE_ATTRIBUTE, StaticComponent.TYPE);
        return theResult;
    }

    public static StaticComponentTemplate deserialize(GameEventManager aEventManager, GameObject aOwner) {
        return new StaticComponentTemplate(aEventManager, aOwner);
    }
}