package de.mirkosertic.gameengine.physics;

import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameKeyCode;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;

public class PlatformComponentTemplate extends GameComponentTemplate<PlatformComponent> {

    private final GameObject owner;

    private final Property<GameKeyCode> moveLeftKey;
    private final Property<GameKeyCode> moveRightKey;
    private final Property<GameKeyCode> jumpKey;
    private final Property<Float> leftRightImpulse;
    private final Property<Float> jumpImpulse;

    public PlatformComponentTemplate(GameEventManager aEventManager, GameObject aOwner) {
        owner = aOwner;
        moveLeftKey = registerProperty(new Property<GameKeyCode>(this, "moveLeftKey", GameKeyCode.LEFT, aEventManager));
        moveRightKey = registerProperty(new Property<GameKeyCode>(this, "moveRightKey", GameKeyCode.RIGHT, aEventManager));
        jumpKey = registerProperty(new Property<GameKeyCode>(this, "jumpKey", GameKeyCode.UP, aEventManager));
        leftRightImpulse = registerProperty(new Property<Float>(this, "leftRightImpulse", 9f, aEventManager));
        jumpImpulse = registerProperty(new Property<Float>(this, "jumpImpulse", 1.4f, aEventManager));
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public Property<GameKeyCode> moveLeftKeyProperty() {
        return moveLeftKey;
    }

    public Property<GameKeyCode> moveRightKeyProperty() {
        return moveRightKey;
    }

    public Property<GameKeyCode> jumpKeyProperty() {
        return jumpKey;
    }

    public Property<Float> leftRightImpulseProperty() {
        return leftRightImpulse;
    }

    public Property<Float> jumpImpulseProperty() {
        return jumpImpulse;
    }

    public PlatformComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        PlatformComponent theSpriteComponent = new PlatformComponent(aInstance, aGameRuntime);
        theSpriteComponent.registerEvents(aGameRuntime);
        return theSpriteComponent;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(PlatformComponent.TYPE_ATTRIBUTE, PlatformComponent.TYPE);
        return theResult;
    }

    public static PlatformComponentTemplate deserialize(GameEventManager aEventManager, GameObject aObject, Map<String, Object> aSerializedData) {
        return new PlatformComponentTemplate(aEventManager, aObject);
    }

    @Override
    public String getTypeKey() {
        return PlatformComponent.TYPE;
    }
}