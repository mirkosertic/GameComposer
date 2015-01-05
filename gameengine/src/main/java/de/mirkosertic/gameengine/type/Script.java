package de.mirkosertic.gameengine.type;

public class Script {

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
}