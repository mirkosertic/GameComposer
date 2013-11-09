package de.mirkosertic.gameengine.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventSheet {

    private List<GameRule> rules;

    public EventSheet() {
        rules = new ArrayList<GameRule>();
    }

    public List<GameRule> getRules() {
        return Collections.unmodifiableList(rules);
    }
}
