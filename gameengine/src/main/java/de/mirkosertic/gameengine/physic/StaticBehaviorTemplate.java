package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.HashMap;
import java.util.Map;

public class StaticBehaviorTemplate implements BehaviorTemplate<StaticBehavior>, Static, Reflectable<StaticClassInformation> {

    private final GameObject owner;

    @UsedByReflection
    public StaticBehaviorTemplate(GameEventManager aEventManager, GameObject aOwner) {
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

    public StaticBehavior create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new StaticBehavior(aInstance);
    }

    @Override
    public String getTypeKey() {
        return StaticBehavior.TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(StaticBehavior.TYPE_ATTRIBUTE, StaticBehavior.TYPE);
        return theResult;
    }

    public static StaticBehaviorTemplate deserialize(GameEventManager aEventManager, GameObject aOwner) {
        return new StaticBehaviorTemplate(aEventManager, aOwner);
    }
}