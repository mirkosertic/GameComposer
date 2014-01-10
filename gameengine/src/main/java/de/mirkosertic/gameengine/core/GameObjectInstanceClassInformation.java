package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.AbsolutePositionAnchor;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Position;

public class GameObjectInstanceClassInformation extends ClassInformation {

    public final static Field<GameObjectInstance, Property<String>> UUID = new Field<GameObjectInstance, Property<String>>("uuid", String.class) {        @Override
        public Property<String> getValue(GameObjectInstance aObject) {
            return aObject.uuid;
        }
    };

    public final static Field<GameObjectInstance, Property<String>> NAME = new Field<GameObjectInstance, Property<String>>("name", Property.class) {
        @Override
        public Property<String> getValue(GameObjectInstance aObject) {
            return aObject.name;
        }
    };

    public final static Field<GameObjectInstance, Property<Position>> POSITION = new Field<GameObjectInstance, Property<Position>>("position", Property.class) {
        @Override
        public Property<Position> getValue(GameObjectInstance aObject) {
            return aObject.position;
        }
    };

    public final static Field<GameObjectInstance, Property<Angle>> ROTATIONANGLE = new Field<GameObjectInstance, Property<Angle>>("rotationAngle", Property.class) {
        @Override
        public Property<Angle> getValue(GameObjectInstance aObject) {
            return aObject.rotationAngle;
        }
    };

    public final static Field<GameObjectInstance, Property<Boolean>> VISIBLE = new Field<GameObjectInstance, Property<Boolean>>("visible", Property.class) {
        @Override
        public Property<Boolean> getValue(GameObjectInstance aObject) {
            return aObject.visible;
        }
    };

    public final static Field<GameObjectInstance, Property<Boolean>> ABSOLUTEPOSITION = new Field<GameObjectInstance, Property<Boolean>>("absolutePosition", Property.class) {
        @Override
        public Property<Boolean> getValue(GameObjectInstance aObject) {
            return aObject.absolutePosition;
        }
    };

    public final static Field<GameObjectInstance, Property<AbsolutePositionAnchor>> ABSOLUTEPOSITIONANCHOR = new Field<GameObjectInstance, Property<AbsolutePositionAnchor>>("absolutePositionAnchor", Property.class) {
        @Override
        public Property<AbsolutePositionAnchor> getValue(GameObjectInstance aObject) {
            return aObject.absolutePositionAnchor;
        }
    };

    public final static GameObjectInstanceClassInformation INSTANCE = new GameObjectInstanceClassInformation();

    private GameObjectInstanceClassInformation() {
        register(UUID);
        register(NAME);
        register(POSITION);
        register(ROTATIONANGLE);
        register(VISIBLE);
        register(ABSOLUTEPOSITION);
        register(ABSOLUTEPOSITIONANCHOR);
    }
}