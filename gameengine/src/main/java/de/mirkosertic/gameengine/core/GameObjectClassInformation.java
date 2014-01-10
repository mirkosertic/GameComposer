package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Size;

public class GameObjectClassInformation extends ClassInformation {

    public final static Field<GameObject, Property<String>> UUID = new Field<GameObject, Property<String>>("uuid", String.class) {
        @Override
        public Property<String> getValue(GameObject aObject) {
            return aObject.uuid;
        }
    };

    public final static Field<GameObject, Property<String>> NAME = new Field<GameObject, Property<String>>("name", Property.class) {
        @Override
        public Property<String> getValue(GameObject aObject) {
            return aObject.name;
        }
    };

    public final static Field<GameObject, Property<Size>> SIZE = new Field<GameObject, Property<Size>>("size", Property.class) {
        @Override
        public Property<Size> getValue(GameObject aObject) {
            return aObject.size;
        }
    };

    public final static GameObjectClassInformation INSTANCE = new GameObjectClassInformation();

    private GameObjectClassInformation() {
        register(NAME);
        register(UUID);
        register(SIZE);
    }
}
