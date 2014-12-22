package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.ScoreValue;

import java.util.HashMap;
import java.util.Map;

public class PlayerScoreBehaviorTemplate implements BehaviorTemplate<PlayerScoreBehavior>, PlayerScore, Reflectable<PlayerScoreClassInformation> {

    private final GameObject owner;
    private final Property<ScoreValue> scoreValue;

    @UsedByReflection
    public PlayerScoreBehaviorTemplate(GameEventManager aEventManager, GameObject aOwner) {
        scoreValue = new Property<>(ScoreValue.class, this, SCORE_VALUE_PROPERTY, new ScoreValue(), aEventManager);
        owner = aOwner;
    }

    @Override
    public PlayerScoreClassInformation getClassInformation() {
        return PlayerScoreClassInformation.INSTANCE;
    }

    public Property<ScoreValue> scoreValueProperty() {
        return scoreValue;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    @Override
    public PlayerScoreBehavior create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new PlayerScoreBehavior(aInstance, this);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(PlayerScoreBehavior.TYPE_ATTRIBUTE, PlayerScoreBehavior.TYPE);
        theResult.put(SCORE_VALUE_PROPERTY, scoreValue.get().serialize());
        return theResult;
    }

    @Override
    public void delete() {
        owner.getGameScene().removeBehaviorFrom(owner, this);
    }

    public static PlayerScoreBehaviorTemplate deserialize(GameObject aOwner, GameEventManager aEventmanager, Map<String, Object> aSerializedData) {
        PlayerScoreBehaviorTemplate theResult = new PlayerScoreBehaviorTemplate(aEventmanager, aOwner);
        Map<String, Object> theScore = (Map<String, Object>) aSerializedData.get(SCORE_VALUE_PROPERTY);
        theResult.scoreValue.setQuietly(ScoreValue.deserialize(theScore));
        return theResult;
    }
}