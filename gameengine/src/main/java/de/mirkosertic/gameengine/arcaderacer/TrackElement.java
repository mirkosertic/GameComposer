package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.Color;

public class TrackElement {

    public final Road road;
    public final double height;
    public final Color terrainColor;
    public final Sprite[] sprites;

    public TrackElement(Road aRoad, double aHeight, Color aTerrainColor, Sprite[] aSprites) {
        road = aRoad;
        height = aHeight;
        terrainColor = aTerrainColor;
        sprites = aSprites;
    }
}