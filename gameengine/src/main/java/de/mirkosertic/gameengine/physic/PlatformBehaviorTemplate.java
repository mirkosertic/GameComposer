package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.HashMap;
import java.util.Map;

public class PlatformBehaviorTemplate implements BehaviorTemplate<PlatformBehavior>, Platform, Reflectable<PlatformClassInformation> {

    private final GameObject owner;

    private final Property<GameKeyCode> moveLeftKey;
    private final Property<GameKeyCode> moveRightKey;
    private final Property<GameKeyCode> jumpKey;
    private final Property<Float> leftRightImpulse;
    private final Property<Float> jumpImpulse;

    @UsedByReflection
    public PlatformBehaviorTemplate(GameEventManager aEventManager, GameObject aOwner) {
        owner = aOwner;
        moveLeftKey = new Property<GameKeyCode>(GameKeyCode.class, this, MOVE_LEFT_KEY_PROPERTY, GameKeyCode.LEFT, aEventManager);
        moveRightKey = new Property<GameKeyCode>(GameKeyCode.class, this, MOVE_RIGHT_KEY_PROPERTY, GameKeyCode.RIGHT, aEventManager);
        jumpKey = new Property<GameKeyCode>(GameKeyCode.class, this, JUMP_KEY_PROPERTY, GameKeyCode.UP, aEventManager);
        leftRightImpulse = new Property<Float>(Float.class, this, LEFT_RIGHT_IMPULSE_PROPERTY, 4f, aEventManager);
        jumpImpulse = new Property<Float>(Float.class, this, JUMP_IMPULSE_PROPERTY, 2f, aEventManager);
    }

    @Override
    public PlatformClassInformation getClassInformation() {
        return PlatformClassInformation.INSTANCE;
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

    public PlatformBehavior create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        PlatformBehavior theSpriteComponent = new PlatformBehavior(aInstance, aGameRuntime);
        theSpriteComponent.registerEvents(aGameRuntime);
        return theSpriteComponent;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(PlatformBehavior.TYPE_ATTRIBUTE, PlatformBehavior.TYPE);
        theResult.put(MOVE_LEFT_KEY_PROPERTY, moveLeftKey.get().name());
        theResult.put(MOVE_RIGHT_KEY_PROPERTY, moveRightKey.get().name());
        theResult.put(JUMP_KEY_PROPERTY, jumpKey.get().name());
        theResult.put(JUMP_IMPULSE_PROPERTY, Float.toString(jumpImpulse.get()));
        theResult.put(LEFT_RIGHT_IMPULSE_PROPERTY, Float.toString(leftRightImpulse.get()));
        return theResult;
    }

    public static PlatformBehaviorTemplate deserialize(GameEventManager aEventManager, GameObject aObject, Map<String, Object> aSerializedData) {
        PlatformBehaviorTemplate theResult =  new PlatformBehaviorTemplate(aEventManager, aObject);
        String theLeftKey = (String) aSerializedData.get(MOVE_LEFT_KEY_PROPERTY);
        if (theLeftKey != null) {
            theResult.moveLeftKey.setQuietly(GameKeyCode.valueOf(theLeftKey));
        }
        String theRightKey = (String) aSerializedData.get(MOVE_RIGHT_KEY_PROPERTY);
        if (theRightKey != null) {
            theResult.moveRightKey.setQuietly(GameKeyCode.valueOf(theRightKey));
        }
        String theJumpKey = (String) aSerializedData.get(JUMP_KEY_PROPERTY);
        if (theJumpKey != null) {
            theResult.jumpKey.setQuietly(GameKeyCode.valueOf(theJumpKey));
        }
        String theJumpImpulse = (String) aSerializedData.get(JUMP_IMPULSE_PROPERTY);
        if (theJumpImpulse != null) {
            theResult.jumpImpulse.setQuietly(Float.parseFloat(theJumpImpulse));
        }
        String theLeftRightUmpulse = (String) aSerializedData.get(LEFT_RIGHT_IMPULSE_PROPERTY);
        if (theLeftRightUmpulse != null) {
            theResult.leftRightImpulse.setQuietly(Float.parseFloat(theLeftRightUmpulse));
        }

        return theResult;
    }

    @Override
    public String getTypeKey() {
        return PlatformBehavior.TYPE;
    }
}