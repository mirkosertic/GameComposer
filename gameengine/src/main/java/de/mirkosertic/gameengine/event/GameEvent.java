package de.mirkosertic.gameengine.event;

import java.util.HashMap;
import java.util.Map;

public class GameEvent {

    private static final Map<String, Object> EMPTY_MAP = new HashMap<String, Object>();

    public final String type;

    protected GameEvent(String aType) {
        type = aType;
    }

    public Map<String, Object> getMemberValues() {
        return EMPTY_MAP;
    }
}