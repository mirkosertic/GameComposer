package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ScoreValue;

public interface PlayerScore {

    String SCORE_VALUE_PROPERTY = "scoreValue";
    String[] EDITABLE_PROPERTIES = {SCORE_VALUE_PROPERTY};

    Property<ScoreValue> scoreValueProperty();
}
