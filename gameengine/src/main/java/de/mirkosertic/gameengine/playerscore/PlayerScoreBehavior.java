package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.ScoreValue;

import java.util.HashMap;
import java.util.Map;

public class PlayerScoreBehavior implements Behavior, PlayerScore, Reflectable<PlayerScoreClassInformation> {

    static final String TYPE = "PlayerScore";

    private final GameObjectInstance objectInstance;
    private final Property<ScoreValue> scoreValue;

    private PlayerScoreBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getComponentTemplate(PlayerScoreBehaviorTemplate.class));
    }

    PlayerScoreBehavior(GameObjectInstance aObjectInstance, PlayerScoreBehaviorTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        scoreValue = new Property<ScoreValue>(ScoreValue.class, this, SCORE_VALUE_PROPERTY, aTemplate.scoreValueProperty().get(), theEventManager);
    }

    @Override
    public PlayerScoreClassInformation getClassInformation() {
        return PlayerScoreClassInformation.INSTANCE;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public PlayerScoreBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(PlayerScoreBehaviorTemplate.class);
    }

    @Override
    public Property<ScoreValue> scoreValueProperty() {
        return scoreValue;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(SCORE_VALUE_PROPERTY, scoreValue.get().serialize());
        return theResult;
    }

    public static PlayerScoreBehavior deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        PlayerScoreBehavior theComponent = new PlayerScoreBehavior(aObjectInstance);
        Map<String, Object> theScore = (Map<String, Object>) aSerializedData.get(SCORE_VALUE_PROPERTY);
        theComponent.scoreValue.setQuietly(ScoreValue.deserialize(theScore));
        return theComponent;
    }
}
