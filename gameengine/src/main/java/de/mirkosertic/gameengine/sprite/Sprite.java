package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Animation;

public interface Sprite {

    String CURRENT_ANIMATION_PROPERTY = "currentAnimation";

    String SPEED_PROPERTY = "speed";

    @ReflectiveField
    Property<Animation> currentAnimationProperty();

    @ReflectiveField
    Property<Integer> speedProperty();

    GameScene getGameScene();

    @ReflectiveMethod
    void delete();

    Animation[] getAnimations();
}