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

public class Speed implements Reflectable, Distributable {

    private static final SpeedClassInformation CIINSTANCE = new SpeedClassInformation();

    public final long speed;

    public Speed(long aSpeed) {
        speed = aSpeed;
    }

    public Long get() {
        return speed;
    }

    @ReflectiveMethod
    public Speed incrementBy(int aAmount) {
        return new Speed(speed + aAmount);
    }

    @ReflectiveMethod
    public Speed resetToZero() {
        return new Speed(0);
    }

    @Override
    public SpeedClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("speed", Long.toString(speed));
        return theResult;
    }

    public static Speed deserialize(Map<String, Object> aSerializedData) {
        long theSpeed = Long.valueOf((String) aSerializedData.get("speed"));
        return new Speed(theSpeed);
    }
}
