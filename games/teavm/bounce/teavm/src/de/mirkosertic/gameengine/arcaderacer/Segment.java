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

import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.ResourceName;

public class Segment {

    public final double xStart;
    public final double yStart;
    public final double xEnd;
    public final double yEnd;
    public final ResourceName texture;
    public final Color color;

    public Segment(double aXStart, double aYStart, double aXEnd, double aYEnd, ResourceName aTexture) {
        xStart = aXStart;
        yStart = aYStart;
        xEnd = aXEnd;
        yEnd = aYEnd;
        texture = aTexture;
        color = null;
    }

    public Segment(double aXStart, double aYStart, double aXEnd, double aYEnd, Color aColor) {
        xStart = aXStart;
        yStart = aYStart;
        xEnd = aXEnd;
        yEnd = aYEnd;
        texture = null;
        color = aColor;
    }
}