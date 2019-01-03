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
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.ResourceName;

public class Track {

    private static final Animation COLUMN = new Animation("Lala");
    private static final Sprite[] SPRITES = new Sprite[] {
        new Sprite(COLUMN, -3, -1, 2, 2, 4)
    };
    private static final Color DARK_GREEN = new Color(0, 200, 0);
    private static final Color LIGHT_GREEN = new Color(0, 180, 0);
    private static final Color DARK_GRAY = new Color(160, 160, 160);
    private static final ResourceName ASPHALT_TEXTURE = new ResourceName("/assets/sprites/ground_asphalt_synth_08.png");

    private static final Segment[] SEGMENTS_EVEN = new Segment[] {
        // Left and right terrain
        new Segment(-100, 0, -2, 0, DARK_GREEN),
                new Segment(2, 0, 100, 0, DARK_GREEN),
                // Left and right curbs
                new Segment(-2, 0 , -1.8, 0, Color.RED),
                new Segment(1.8, 0, 2, 0, Color.RED),
                // Left and right roads
                new Segment(-1.8, 0, -0.05, 0, ASPHALT_TEXTURE),
                new Segment(0.05, 0, 1.8, 0, ASPHALT_TEXTURE),
                // Center line
                new Segment(-0.05, 0, 0.05, 0, DARK_GRAY),
    };

    private static final Segment[] SEGMENTS_ODD = new Segment[] {
            // Left and right terrain
            new Segment(-100, 0, -2, 0, LIGHT_GREEN),
            new Segment(2, 0, 100, 0, LIGHT_GREEN),
            // Left and right curbs
            new Segment(-2, 0 , -1.8, 0, Color.WHITE),
            new Segment(1.8, 0, 2, 0, Color.WHITE),
            // Left and right roads
            new Segment(-1.8, 0, -0.05, 0, ASPHALT_TEXTURE),
            new Segment(0.05, 0, 1.8, 0, ASPHALT_TEXTURE),
            // Center line
            new Segment(-0.05, 0, 0.05, 0, Color.YELLOW),
    };


    public TrackElement getTrackElementForPosition(int aPosition) {
        int theCurveFactor = 2;
        double theHeight = (Math.cos(Math.toRadians(aPosition * 10)) * 1);
        return new TrackElement(theCurveFactor, aPosition % 2 == 0 ? SEGMENTS_ODD : SEGMENTS_EVEN, theHeight, SPRITES, (int) (Math.cos(Math.toRadians(aPosition * 10)) * 10));
    }
}