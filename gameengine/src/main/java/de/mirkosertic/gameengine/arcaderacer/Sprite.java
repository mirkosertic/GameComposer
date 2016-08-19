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
