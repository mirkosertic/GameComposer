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

import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

import java.util.HashMap;
import java.util.Map;

public class Angle implements Reflectable, Distributable {

    private static final AngleClassInformation CIINSTANCE = new AngleClassInformation();

    public final int angleInDegrees;

    public static final Angle ZERO = new Angle(0);
    private Angle inverted;

    public Angle(int aAngleInDegrees) {
        while (aAngleInDegrees < 0) {
            aAngleInDegrees += 360;
        }
        angleInDegrees = aAngleInDegrees % 360;
    }

    public Integer get() {
        return angleInDegrees;
    }

    @ReflectiveMethod
    public Angle add(int aDiff) {
        return new Angle(angleInDegrees + aDiff);
    }

    @ReflectiveMethod
    public Angle invert() {
        if (inverted == null) {
            inverted = new Angle(-angleInDegrees);
        }
        return inverted;
    }

    @ReflectiveMethod
    public Angle snapTo(int aStep) {
        int theStepHalf = aStep / 2;
        for (int i = 0;i<=360 / aStep;i++) {
            int theStep = i * aStep;
            int theMinAngle = theStep - theStepHalf;
            int theMaxAngle = theStep + theStepHalf;
            if (angleInDegrees >=theMinAngle && angleInDegrees < theMaxAngle) {
                return new Angle(theStep);
            }
        }
        return new Angle(angleInDegrees);
    }

    @ReflectiveMethod
    public Angle resetToZero() {
        return new Angle(0);
    }

    public float toRadians() {
        return (float) Math.toRadians(angleInDegrees);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Angle angle = (Angle) o;

        return angleInDegrees == angle.angleInDegrees;

    }

    @Override
    public int hashCode() {
        return angleInDegrees;
    }

    @Override
    public AngleClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("angleindegrees", Integer.toString(angleInDegrees));
        return theResult;
    }

    public static Angle deserialize(Map<String, Object> aSerializedData) {
        int theAngleInDegrees = Integer.parseInt((String) aSerializedData.get("angleindegrees"));
        return new Angle(theAngleInDegrees);
    }

    public static Angle fromRadians(float aAngleInRadians) {
        return new Angle((int) Math.toDegrees(aAngleInRadians));
    }
}