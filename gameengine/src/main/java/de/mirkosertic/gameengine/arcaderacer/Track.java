package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.ResourceName;

public class Track {

    public TrackElement getTrackElementForPosition(int aPosition) {
        int theCurveFactor = 2;
        float theNew = (float) (Math.cos(Math.toRadians(aPosition * 10)) * 1);
        Curb[] theCurbs = new Curb[] {
                new Curb(-2, 0.1, aPosition %2 == 0 ? new Color(255, 0, 0) : new Color(255, 255, 255)),
                new Curb(-0.05, 0.1, aPosition %2 == 0 ? new Color(160, 160, 160) : new Color(255, 255, 0)),
                new Curb(1.9, 0.1, aPosition %2 == 0 ? new Color(255, 0, 0) : new Color(255, 255, 255))
        };
        Animation theAnimation = new Animation("Lala").addToAnimationSequence(new ResourceName("column.png"));
        Sprite[] theSprites = new Sprite[] {
            new Sprite(theAnimation, -3, -1, 2, 2, 4)
        };
        return new TrackElement(new Road(-2f, 2f, theCurveFactor, aPosition % 2 == 0 ? new Color(160, 160, 160) : new Color(180, 180, 180), theCurbs), theNew, aPosition % 2 == 0 ? new Color(0, 200, 0): new Color(0, 180, 0), theSprites);
    }
}
