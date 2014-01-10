package de.mirkosertic.gameengine.playerscore;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.ScoreValue;

public class PlayerScoreClassInformation extends ClassInformation {

    public final static Field<PlayerScore, Property<ScoreValue>> SCOREVALUE = new Field<PlayerScore, Property<ScoreValue>>("scoreValue", Property.class) {
        @Override
        public Property<ScoreValue> getValue(PlayerScore aObject) {
            return aObject.scoreValueProperty();
        }
    };

    public static final PlayerScoreClassInformation INSTANCE = new PlayerScoreClassInformation();

    private PlayerScoreClassInformation() {
        register(SCOREVALUE);
    }
}
