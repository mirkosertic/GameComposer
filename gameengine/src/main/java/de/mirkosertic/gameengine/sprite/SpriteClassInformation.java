package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.ResourceName;

public class SpriteClassInformation extends ClassInformation {

    public static final SpriteClassInformation INSTANCE = new SpriteClassInformation();

    public final static Field<Sprite, Property<ResourceName>> RESOURCENAME = new Field<Sprite, Property<ResourceName>>("resourceName", Property.class) {
        @Override
        public Property<ResourceName> getValue(Sprite aObject) {
            return aObject.resourceNameProperty();
        }
    };

    private SpriteClassInformation() {
        register(RESOURCENAME);
    }
}
