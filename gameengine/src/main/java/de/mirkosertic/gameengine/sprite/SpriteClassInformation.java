package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;

public class SpriteClassInformation extends ClassInformation {

    public final static Field<Sprite, Property<Animation>> CURRENTANIMATION = new Field<Sprite, Property<Animation>>("currentAnimation", Property.class) {
        @Override
        public Property<Animation> getValue(Sprite aObject) {
            return aObject.currentAnimationProperty();
        }
    };

    public final static Field<Sprite, Property<Integer>> SPEED = new Field<Sprite, Property<Integer>>("speed", Property.class) {
        @Override
        public Property<Integer> getValue(Sprite aObject) {
            return aObject.speedProperty();
        }
    };

    public static final SpriteClassInformation INSTANCE = new SpriteClassInformation();

    private SpriteClassInformation() {
        register(CURRENTANIMATION);
        register(SPEED);
    }
}
