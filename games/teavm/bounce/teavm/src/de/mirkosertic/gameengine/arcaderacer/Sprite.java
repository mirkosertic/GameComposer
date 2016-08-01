package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.ResourceName;

public class Sprite {

    public final Animation animation;
    public final double positionX;
    public final double positionY;
    public final double width;
    public final double height;
    public final int animationSpeed;

    public Sprite(Animation aAnimation, double aPositionX, double aPositionY, double aWidth, double aHeight, int aAnimationSpeed) {
        animation = aAnimation;
        positionX = aPositionX;
        positionY = aPositionY;
        width = aWidth;
        height = aHeight;
        animationSpeed = aAnimationSpeed;
    }

    public ResourceName computeCurrentView(long aGameTime) {
        return animation.computeCurrentFrame(aGameTime, animationSpeed);
    }
}
