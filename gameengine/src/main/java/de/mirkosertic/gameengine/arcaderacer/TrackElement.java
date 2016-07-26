package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.Color;

public class TrackElement {

    public final Road road;
    public final float height;
    public final Color terrainColor;
    public final Sprite[] sprites;

    public TrackElement(Road aRoad, float aHeight, Color aTerrainColor, Sprite[] aSprites) {
        road = aRoad;
        height = aHeight;
        terrainColor = aTerrainColor;
        sprites = aSprites;
    }
}