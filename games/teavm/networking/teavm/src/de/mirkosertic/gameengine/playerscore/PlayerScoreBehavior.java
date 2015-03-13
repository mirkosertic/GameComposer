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

    private static final PlayerScoreClassInformation CIINSTANCE = new PlayerScoreClassInformation();

    static final String TYPE = "PlayerScore";

    private final GameObjectInstance objectInstance;
    private final Property<ScoreValue> scoreValue;

    private PlayerScoreBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getBehaviorTemplate(PlayerScoreBehaviorTemplate.class));
    }

    PlayerScoreBehavior(GameObjectInstance aObjectInstance, PlayerScoreBehaviorTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        scoreValue = new Property<>(ScoreValue.class, this, SCORE_VALUE_PROPERTY, aTemplate.scoreValueProperty().get(), theEventManager);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public PlayerScoreClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public PlayerScoreBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getBehaviorTemplate(PlayerScoreBehaviorTemplate.class);
    }

    @Override
    public Property<ScoreValue> scoreValueProperty() {
        return scoreValue;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(SCORE_VALUE_PROPERTY, scoreValue.get().serialize());
        return theResult;
    }

    @Override
    public void delete() {
        objectInstance.getOwnerGameObject().getGameScene().removeBehaviorFrom(objectInstance.getOwnerGameObject(), this);
    }

    @Override
    public GameObjectInstance getInstance() {
        return objectInstance;
    }

    @Override
    public void markAsRemoteObject() {
    }

    public static PlayerScoreBehavior deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        PlayerScoreBehavior theComponent = new PlayerScoreBehavior(aObjectInstance);
        Map<String, Object> theScore = (Map<String, Object>) aSerializedData.get(SCORE_VALUE_PROPERTY);
        theComponent.scoreValue.setQuietly(ScoreValue.deserialize(theScore));
        return theComponent;
    }
}
