package de.mirkosertic.gameengine.arcaderacer;

public class TrackElement {

    private final Road road;
    private final float height;

    public TrackElement(Road aRoad, float aHeight) {
        road = aRoad;
        height = aHeight;
    }

    public Road getRoad() {
        return road;
    }

    public float getHeight() {
        return height;
    }
}
