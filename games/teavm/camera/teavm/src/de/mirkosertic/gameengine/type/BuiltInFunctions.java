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

public abstract class BuiltInFunctions implements Reflectable {

    @ReflectiveMethod
    public Number systime() {
        return System.currentTimeMillis();
    }

    @ReflectiveMethod
    public abstract String formatTime(Number aTimeInMilis, String aPattern);

    @ReflectiveMethod
    public Number max(Number aNumber1, Number aNumber2) {
        if (aNumber1 instanceof Integer) {
            return Math.max(aNumber1.intValue(), aNumber2.intValue());
        }
        return Math.max(aNumber1.doubleValue(), aNumber2.doubleValue());
    }

    @ReflectiveMethod
    public Number min(Number aNumber1, Number aNumber2) {
        if (aNumber1 instanceof Integer) {
            return Math.min(aNumber1.intValue(), aNumber2.intValue());
        }
        return Math.min(aNumber1.doubleValue(), aNumber2.doubleValue());
    }

    @ReflectiveMethod
    public Object nvl(Object aObject, Object aDefault) {
        if (aObject == null) {
            return aDefault;
        }
        return aObject;
    }
}