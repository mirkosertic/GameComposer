package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.*;

import java.util.HashMap;
import java.util.Map;

public class PlatformComponentTemplate implements GameComponentTemplate<PlatformComponent> {

    private GameObject owner;

    private GameKeyCode moveLeftKey;
    private GameKeyCode moveRightKey;
    private GameKeyCode jumpKey;
    private float leftRightImpulse;
    private float jumpImpulse;

    public PlatformComponentTemplate(GameObject aOwner) {
        owner = aOwner;
        moveLeftKey = GameKeyCode.LEFT;
        moveRightKey = GameKeyCode.RIGHT;
        jumpKey = GameKeyCode.UP;
        leftRightImpulse = 9f;
        jumpImpulse = 1.4f;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    public GameKeyCode getMoveLeftKey() {
        return moveLeftKey;
    }

    public void setMoveLeftKey(GameKeyCode moveLeftKey) {
        this.moveLeftKey = moveLeftKey;
    }

    public GameKeyCode getMoveRightKey() {
        return moveRightKey;
    }

    public void setMoveRightKey(GameKeyCode moveRightKey) {
        this.moveRightKey = moveRightKey;
    }

    public GameKeyCode getJumpKey() {
        return jumpKey;
    }

    public void setJumpKey(GameKeyCode jumpKey) {
        this.jumpKey = jumpKey;
    }

    public float getLeftRightImpulse() {
        return leftRightImpulse;
    }

    public void setLeftRightImpulse(float leftRightImpulse) {
        this.leftRightImpulse = leftRightImpulse;
    }

    public float getJumpImpulse() {
        return jumpImpulse;
    }

    public void setJumpImpulse(float jumpImpulse) {
        this.jumpImpulse = jumpImpulse;
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
