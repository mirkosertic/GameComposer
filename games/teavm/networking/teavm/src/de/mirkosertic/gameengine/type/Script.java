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

import java.util.HashMap;
import java.util.Map;

public class Script implements Distributable {

    public final String script;

    public Script(String aScript) {
        script = aScript;
    }

    @Override
    public boolean equals(Object aValue) {
        if (this == aValue) {
            return true;
        }
        if (aValue == null || getClass() != aValue.getClass()) {
            return false;
        }

        Script script1 = (Script) aValue;

        if (!script.equals(script1.script)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return script.hashCode();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theData = new HashMap<>();
        theData.put("script", script);
        return theData;
    }

    public static Script deserialize(Map<String, Object> aData) {
        return new Script((String) aData.get("script"));
    }
}