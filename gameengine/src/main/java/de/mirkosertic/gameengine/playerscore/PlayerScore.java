package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ScoreValue;

public interface PlayerScore {

    String SCORE_VALUE_PROPERTY = "scoreValue";

    @ReflectiveField
    Property<ScoreValue> scoreValueProperty();

    @ReflectiveMethod
    void delete();
}
