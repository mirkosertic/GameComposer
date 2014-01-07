package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.core.GameComponentTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ScoreValue;

import java.util.HashMap;
import java.util.Map;

public class PlayerScoreComponentTemplate extends GameComponentTemplate<PlayerScoreComponent> implements PlayerScore {

    private final GameObject owner;
    private final Property<ScoreValue> scoreValue;

    public PlayerScoreComponentTemplate(GameEventManager aEventManager, GameObject aOwner) {
        scoreValue = registerProperty(new Property<ScoreValue>(ScoreValue.class, this, SCORE_VALUE_PROPERTY, new ScoreValue(), aEventManager));
        owner = aOwner;
    }

    @Override
    public String getTypeKey() {
        return PlayerScoreComponent.TYPE;
    }

    public Property<ScoreValue> scoreValueProperty() {
        return scoreValue;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    @Override
    public PlayerScoreComponent create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new PlayerScoreComponent(aInstance, this);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(PlayerScoreComponent.TYPE_ATTRIBUTE, PlayerScoreComponent.TYPE);
        theResult.put(SCORE_VALUE_PROPERTY, scoreValue.get().serialize());
        return theResult;
    }

    public static PlayerScoreComponentTemplate deserialize(GameObject aOwner, GameEventManager aEventmanager, Map<String, Object> aSerializedData) {
        PlayerScoreComponentTemplate theResult = new PlayerScoreComponentTemplate(aEventmanager, aOwner);
        Map<String, Object> theScore = (Map<String, Object>) aSerializedData.get(SCORE_VALUE_PROPERTY);
        theResult.scoreValue.setQuietly(ScoreValue.deserialize(theScore));
        return theResult;
    }
}