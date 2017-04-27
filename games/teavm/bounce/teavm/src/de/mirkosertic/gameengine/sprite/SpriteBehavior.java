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
package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;

public class SpriteBehavior implements Behavior, Sprite, Reflectable<SpriteClassInformation> {

    private static final SpriteClassInformation CIINSTANCE = new SpriteClassInformation();

    public static final BehaviorType TYPE = new BehaviorType("Sprite");

    private final GameObjectInstance objectInstance;
    private final Property<Animation> currentAnimation;
    private final Property<Integer> speed;

    private SpriteBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, (SpriteBehaviorTemplate) aObjectInstance.getOwnerGameObject().getBehaviorTemplate(SpriteBehaviorTemplate.TYPE));
    }

    SpriteBehavior(GameObjectInstance aObjectInstance, SpriteBehaviorTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        currentAnimation = new Property<>(Animation.class, this, CURRENT_ANIMATION_PROPERTY, aTemplate.currentAnimationProperty().get(), theEventManager);
        speed = new Property<>(Integer.class, this, SPEED_PROPERTY, aTemplate.speedProperty().get(), theEventManager);
    }

    @Override
    public BehaviorType getType() {
        return TYPE;
    }

    @Override
    public SpriteClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public Property<Animation> currentAnimationProperty() {
        return currentAnimation;
    }

    @Override
    public Property<Integer> speedProperty() {
        return speed;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public SpriteBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getBehaviorTemplate(SpriteBehaviorTemplate.TYPE);
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
    public GameScene getGameScene() {
        return getTemplate().getGameScene();
    }

    @Override
    public Animation[] getAnimations() {
        return getTemplate().getAnimations();
    }

    @Override
    public void markAsRemoteObject() {
    }

    public static SpriteBehavior deserialize(GameObjectInstance aObjectInstance) {
        return new SpriteBehavior(aObjectInstance);
    }

    public ResourceName computeCurrentFrame(long aGameTime) {
        Animation theCurrentAnimation = currentAnimation.get();
        if (theCurrentAnimation != null) {
            return theCurrentAnimation.computeCurrentFrame(aGameTime, speed.get());
        }
        return null;
    }
}