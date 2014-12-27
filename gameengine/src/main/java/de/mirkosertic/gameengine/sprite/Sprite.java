package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Animation;

public interface Sprite {

    String CURRENT_ANIMATION_PROPERTY = "currentAnimation";

    String SPEED_PROPERTY = "speed";

    Property<Animation> currentAnimationProperty();

    Property<Integer> speedProperty();

    GameScene getGameScene();

    void delete();

    Animation[] getAnimations();
}