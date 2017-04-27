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

import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectConfigurationChanged;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpriteBehaviorTemplate implements BehaviorTemplate<SpriteBehavior>, Sprite, Reflectable<SpriteClassInformation> {

    public static final BehaviorType TYPE = new BehaviorType("SpriteBehaviorTemplate");;

    private static final SpriteClassInformation CIINSTANCE = new SpriteClassInformation();

    private final Property<Animation> currentAnimation;
    private final Property<Integer> speed;
    private final List<Animation> animations;
    private final GameObject owner;
    private final GameEventManager eventManager;

    public SpriteBehaviorTemplate(GameEventManager aEventManager, GameObject aOwner) {
        eventManager = aEventManager;
        currentAnimation = new Property<>(Animation.class, this, CURRENT_ANIMATION_PROPERTY, aEventManager);
        speed = new Property<>(Integer.class, this, SPEED_PROPERTY, 5, aEventManager);
        animations = new ArrayList<>();
        owner = aOwner;
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
    public GameObject getOwner() {
        return owner;
    }

    @Override
    public SpriteBehavior create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new SpriteBehavior(aInstance, this);
    }

    @Override
    public Property<Animation> currentAnimationProperty() {
        return currentAnimation;
    }

    @Override
    public Property<Integer> speedProperty() {
        return speed;
    }

    public void addAnimation(Animation aAnimation) {
        animations.add(aAnimation);
        if (currentAnimation.isNull()) {
            currentAnimation.set(aAnimation);
        }
        eventManager.fire(new GameObjectConfigurationChanged(getOwner()));
    }

    private Animation getAnimationByUUID(String aAnimationUUID) {
        for (Animation theAnimation : animations) {
            if (aAnimationUUID.equals(theAnimation.getUuid())) {
                return theAnimation;
            }
        }
        throw new IllegalArgumentException("Unknown UUID");
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(SpriteBehavior.TYPE_ATTRIBUTE, SpriteBehavior.TYPE);
        List<Map<String, Object>> theAnimations = new ArrayList<>();
        for (Animation theAnimation : animations) {
            theAnimations.add(theAnimation.serialize());
        }
        theResult.put("animations", theAnimations);
        if (!currentAnimation.isNull()) {
            theResult.put("currentAnimation", currentAnimation.get().getUuid());
        }
        theResult.put("speed", Integer.toString(speed.get()));
        return theResult;
    }

    @Override
    public void delete() {
        owner.getGameScene().removeBehaviorFrom(owner, this);
    }

    @Override
    public GameScene getGameScene() {
        return owner.getGameScene();
    }

    @Override
    public Animation[] getAnimations() {
        return animations.toArray(new Animation[animations.size()]);
    }

    public void replaceAnimation(Animation aOldAnimation, Animation aNewAnimation) {
        if (aOldAnimation == aNewAnimation) {
            // Nothing to do
            return;
        }
        List<Animation> theNewList = new ArrayList<>();
        for (Animation theAnim : animations) {
            if (aOldAnimation.getUuid().equals(theAnim.getUuid())) {
                theNewList.add(aNewAnimation);
            } else {
                theNewList.add(theAnim);
            }
        }
        animations.clear();
        animations.addAll(theNewList);

        Animation theCurrent = currentAnimation.get();
        if (theCurrent != null) {
            if (theCurrent.getUuid().equals(aOldAnimation.getUuid())) {
                currentAnimation.set(aNewAnimation);
            }
        }

        eventManager.fire(new GameObjectConfigurationChanged(getOwner()));
    }

    public static SpriteBehaviorTemplate deserialize(GameEventManager aEventManager, GameObject aOwner, Map<String, Object> aSerializedData) {
        SpriteBehaviorTemplate theResult = new SpriteBehaviorTemplate(aEventManager, aOwner);
        Map<String, Object> theResourceName = (Map<String, Object>) aSerializedData.get("resourcename");
        if (theResourceName != null) {
            Animation theAnimation = new Animation("Default").addToAnimationSequence(ResourceName.deserialize(theResourceName));
            theResult.animations.add(theAnimation);
            theResult.currentAnimationProperty().setQuietly(theAnimation);
        } else {
            List<Map<String, Object>> theAnimations = (List<Map<String, Object>>) aSerializedData.get("animations");
            for (Map<String, Object> theAnimationData : theAnimations) {
                theResult.animations.add(Animation.deserialize(theAnimationData));
            }
            String theCurrentAnimation = (String) aSerializedData.get("currentAnimation");
            if (theCurrentAnimation != null) {
                theResult.currentAnimationProperty().setQuietly(theResult.getAnimationByUUID(theCurrentAnimation));
            }
            theResult.speed.setQuietly(Integer.valueOf((String) aSerializedData.get("speed")));
        }
        return theResult;
    }
}