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

public class PlatformComponentTemplate implements GameComponentTemplate<PlatformComponent> {

    private GameObject owner;

    private Property<GameKeyCode> moveLeftKey;
    private Property<GameKeyCode> moveRightKey;
    private Property<GameKeyCode> jumpKey;
    private Property<Float> leftRightImpulse;
    private Property<Float> jumpImpulse;

    public PlatformComponentTemplate(GameObject aOwner) {

        GameEventManager theEventManager = aOwner.getGameScene().getRuntime().getEventManager();

        owner = aOwner;
        moveLeftKey = new Property<GameKeyCode>(this, "moveLeftKey", GameKeyCode.LEFT, theEventManager);
        moveRightKey = new Property<GameKeyCode>(this, "moveRightKey", GameKeyCode.RIGHT, theEventManager);
        jumpKey = new Property<GameKeyCode>(this, "jumpKey", GameKeyCode.UP, theEventManager);
        leftRightImpulse = new Property<Float>(this, "leftRightImpulse", 9f, theEventManager);
        jumpImpulse = new Property<Float>(this, "jumpImpulse", 1.4f, theEventManager);
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

    public static PlatformComponentTemplate deserialize(GameObject aObject, Map<String, Object> aSerializedData) {
        return new PlatformComponentTemplate(aObject);
    }

    @Override
    public String getTypeKey() {
        return PlatformComponent.TYPE;
    }
}