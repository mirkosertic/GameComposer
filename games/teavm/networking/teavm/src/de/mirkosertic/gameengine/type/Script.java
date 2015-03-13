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