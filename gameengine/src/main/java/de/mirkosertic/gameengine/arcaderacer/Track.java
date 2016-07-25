package de.mirkosertic.gameengine.arcaderacer;

public class Track {

    public TrackElement getTrackElementForPosition(int aPosition) {
        int theCurveFactor = 2;

        float theNew = (float) (Math.cos(Math.toRadians(aPosition * 20)) * 0.5);

        return new TrackElement(new Road(-2f, 2f, 0.1f, theCurveFactor), theNew);
    }
}
