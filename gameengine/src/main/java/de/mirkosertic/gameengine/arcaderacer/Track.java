package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.ResourceName;

public class Track {

    private static final Animation COLUMN = new Animation("Lala").addToAnimationSequence(new ResourceName("/assets/sprites/column.png"));
    private static final Sprite[] SPRITES = new Sprite[] {
        new Sprite(COLUMN, -3, -1, 2, 2, 4)
    };

    public TrackElement getTrackElementForPosition(int aPosition) {
        int theCurveFactor = 2;
        double theHeight = (Math.cos(Math.toRadians(aPosition * 10)) * 1);
        Segment[] theSegments = new Segment[] {
            // Left and right terrain
            new Segment(-100, 0, -2, 0, aPosition % 2 == 0 ? new Color(0, 200, 0) : new Color(0, 180, 0)),
            new Segment(2, 0, 100, 0, aPosition % 2 == 0 ? new Color(0, 200, 0) : new Color(0, 180, 0)),
            // Left and right curbs
            new Segment(-2, 0 , -1.8, 0, aPosition % 2 == 0 ? new Color(255, 0, 0) : new Color(255, 255, 255)),
            new Segment(1.8, 0, 2, 0, aPosition % 2 == 0 ? new Color(255, 0, 0) : new Color(255, 255, 255)),
            // Left and right roads
            new Segment(-1.8, 0, -0.05, 0, new ResourceName("/assets/sprites/ground_asphalt_synth_08.png")),
            new Segment(0.05, 0, 1.8, 0, new ResourceName("/assets/sprites/ground_asphalt_synth_08.png")),
            // Center line
            new Segment(-0.05, 0, 0.05, 0, aPosition %2 == 0 ? new Color(160, 160, 160) : new Color(255, 255, 0)),
        };
        return new TrackElement(theCurveFactor, theSegments, theHeight, SPRITES, (int) (Math.cos(Math.toRadians(aPosition * 10)) * 10));
    }
}