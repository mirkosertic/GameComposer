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

public class TrackElement {

    public final int curveFactor;
    public final Segment[] segments;
    public final double height;
    public final Sprite[] sprites;
    public final int angle;

    public TrackElement(int aCurveFactor, Segment[] aSegments, double aHeight, Sprite[] aSprites, int aAngle) {
        curveFactor = aCurveFactor;
        segments = aSegments;
        height = aHeight;
        sprites = aSprites;
        angle = aAngle;
    }
}