package de.mirkosertic.gameengine.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameRule {

    private Condition condition;
    private List<Action> actions = new ArrayList<Action>();

    public GameRule(Condition aCondition) {
        condition = aCondition;
    }

    public Condition getCondition() {
        return condition;
    }

    public List<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }
}
