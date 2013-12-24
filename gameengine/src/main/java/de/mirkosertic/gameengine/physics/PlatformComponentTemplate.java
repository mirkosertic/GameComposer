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

public class PlatformComponentTemplate extends GameComponentTemplate<PlatformComponent> implements Platform {

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
        leftRightImpulse = registerProperty(new Property<Float>(this, "leftRightImpulse", 4f, aEventManager));
        jumpImpulse = registerProperty(new Property<Float>(this, "jumpImpulse", 2f, aEventManager));
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
        PlatformComponent theSpriteComponent = new PlatformComponent(aInstance, aGameRuntime, this);
        theSpriteComponent.registerEvents(aGameRuntime);
        return theSpriteComponent;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(PlatformComponent.TYPE_ATTRIBUTE, PlatformComponent.TYPE);
        theResult.put("moveLeftKey", moveLeftKey.get().name());
        theResult.put("moveRightKey", moveRightKey.get().name());
        theResult.put("jumpKey", jumpKey.get().name());
        theResult.put("jumpImpulse", Float.toString(jumpImpulse.get()));
        theResult.put("leftRightImpulse", Float.toString(leftRightImpulse.get()));
        return theResult;
    }

    public static PlatformComponentTemplate deserialize(GameEventManager aEventManager, GameObject aObject, Map<String, Object> aSerializedData) {
        PlatformComponentTemplate theResult =  new PlatformComponentTemplate(aEventManager, aObject);
        String theLeftKey = (String) aSerializedData.get("moveLeftKey");
        if (theLeftKey != null) {
            theResult.moveLeftKey.setQuietly(GameKeyCode.valueOf(theLeftKey));
        }
        String theRightKey = (String) aSerializedData.get("moveRightKey");
        if (theRightKey != null) {
            theResult.moveRightKey.setQuietly(GameKeyCode.valueOf(theRightKey));
        }
        String theJumpKey = (String) aSerializedData.get("jumpKey");
        if (theJumpKey != null) {
            theResult.jumpKey.setQuietly(GameKeyCode.valueOf(theJumpKey));
        }
        String theJumpImpulse = (String) aSerializedData.get("jumpImpulse");
        if (theJumpImpulse != null) {
            theResult.jumpImpulse.setQuietly(Float.parseFloat(theJumpImpulse));
        }
        String theLeftRightUmpulse = (String) aSerializedData.get("leftRightImpulse");
        if (theLeftRightUmpulse != null) {
            theResult.leftRightImpulse.setQuietly(Float.parseFloat(theLeftRightUmpulse));
        }

        return theResult;
    }

    @Override
    public String getTypeKey() {
        return PlatformComponent.TYPE;
    }
}