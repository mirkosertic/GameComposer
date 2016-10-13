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
package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

import java.util.HashMap;
import java.util.Map;

public class Position implements Reflectable<PositionClassInformation>, Distributable {

    private static final PositionClassInformation CIINSTANCE = new PositionClassInformation();

    @ReflectiveField
    public final float x;

    @ReflectiveField
    public final float y;

    public Position() {
        this(0f, 0f);
    }

    public Position(float aX, float aY) {
        x = aX;
        y = aY;
    }

    public Position(double aX, double aY) {
        x = (float) aX;
        y = (float) aY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        return ((int) x) == (int) position.x &&
                ((int) y) == (int) position.y;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public PositionClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @ReflectiveMethod
    public Position add(Position aOffset) {
        return new Position(x + aOffset.x, y + aOffset.y);
    }

    @ReflectiveMethod
    public Position changeX(Float aNewX) {
        if (aNewX != x) {
            return new Position(aNewX, y);
        }
        return this;
    }

    @ReflectiveMethod
    public Position changeY(Float aNewY) {
        if (aNewY != y) {
            return new Position(x, aNewY);
        }
        return this;
    }

    @ReflectiveMethod
    public Position snapToGrid(int aGridWidth, int aGridHeight) {
        float theSnapX = x - x % aGridWidth;
        float theSnapY = y - y % aGridHeight;
        return new Position(theSnapX, theSnapY);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("x", Float.toString(x));
        theResult.put("y", Float.toString(y));
        return theResult;
    }

    public static Position deserialize(Map<String, Object> aSerializedStructure) {
        float theX = Float.valueOf((String) aSerializedStructure.get("x"));
        float theY = Float.valueOf((String) aSerializedStructure.get("y"));
        return new Position(theX, theY);
    }

    public Position translate(Angle aAngle, double aDistance) {
        float theRadians = aAngle.toRadians();
        double theMX = Math.sin(theRadians) * aDistance;
        double theMY = Math.cos(theRadians) * aDistance;
        return new Position(x + theMX, y - theMY);
    }
}
