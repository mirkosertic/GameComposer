package de.mirkosertic.gameengine.starfield;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;

public class StarfieldGameSceneEffectClassInformation extends ClassInformation {

    public final static Field<StarfieldGameSceneEffect, Property<Integer>> NUMBER_OF_STARS = new Field<StarfieldGameSceneEffect, Property<Integer>>("numberOfStars", Property.class) {
        @Override
        public Property<Integer> getValue(StarfieldGameSceneEffect aObject) {
            return aObject.numberofStars();
        }
    };

    public static final StarfieldGameSceneEffectClassInformation INSTANCE = new StarfieldGameSceneEffectClassInformation();

    private StarfieldGameSceneEffectClassInformation() {
        register(NUMBER_OF_STARS);
    }
}