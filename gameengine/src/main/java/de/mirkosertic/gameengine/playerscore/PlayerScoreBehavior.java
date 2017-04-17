/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.ScoreValue;

import java.util.HashMap;
import java.util.Map;

public class PlayerScoreBehavior implements Behavior, PlayerScore, Reflectable<PlayerScoreClassInformation> {

    private static final PlayerScoreClassInformation CIINSTANCE = new PlayerScoreClassInformation();

    public static final BehaviorType TYPE = new BehaviorType("PlayerScore");

    private final GameObjectInstance objectInstance;
    private final Property<ScoreValue> scoreValue;

    private PlayerScoreBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, (PlayerScoreBehaviorTemplate) aObjectInstance.getOwnerGameObject().getBehaviorTemplate(PlayerScoreBehaviorTemplate.TYPE));
    }

    PlayerScoreBehavior(GameObjectInstance aObjectInstance, PlayerScoreBehaviorTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        scoreValue = new Property<>(ScoreValue.class, this, SCORE_VALUE_PROPERTY, aTemplate.scoreValueProperty().get(), theEventManager);
    }

    @Override
    public BehaviorType getType() {
        return TYPE;
    }

    @Override
    public PlayerScoreClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public PlayerScoreBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getBehaviorTemplate(PlayerScoreBehaviorTemplate.TYPE);
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
