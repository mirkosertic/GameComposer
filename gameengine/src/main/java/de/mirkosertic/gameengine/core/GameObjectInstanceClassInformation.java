package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.*;

public class GameObjectInstanceClassInformation extends ClassInformation {

    public final static Field<GameObjectInstance, Property<String>> UUID = new Field<GameObjectInstance, Property<String>>("uuid", Property.class) {
        @Override
        public Property<String> getValue(GameObjectInstance aObject) {
            return aObject.uuidProperty();
        }
    };

    public final static Field<GameObjectInstance, Property<String>> NAME = new Field<GameObjectInstance, Property<String>>("name", Property.class) {
        @Override
        public Property<String> getValue(GameObjectInstance aObject) {
            return aObject.nameProperty();
        }
    };

    public final static Field<GameObjectInstance, Property<Position>> POSITION = new Field<GameObjectInstance, Property<Position>>("position", Property.class) {
        @Override
        public Property<Position> getValue(GameObjectInstance aObject) {
            return aObject.positionProperty();
        }
    };

    public final static Field<GameObjectInstance, Property<Angle>> ROTATIONANGLE = new Field<GameObjectInstance, Property<Angle>>("rotationAngle", Property.class) {
        @Override
        public Property<Angle> getValue(GameObjectInstance aObject) {
            return aObject.rotationAngleProperty();
        }
    };

    public final static Field<GameObjectInstance, Property<Boolean>> VISIBLE = new Field<GameObjectInstance, Property<Boolean>>("visible", Property.class) {
        @Override
        public Property<Boolean> getValue(GameObjectInstance aObject) {
            return aObject.visibleProperty();
        }
    };

    public final static Field<GameObjectInstance, Property<Boolean>> ABSOLUTEPOSITION = new Field<GameObjectInstance, Property<Boolean>>("absolutePosition", Property.class) {
        @Override
        public Property<Boolean> getValue(GameObjectInstance aObject) {
            return aObject.absolutePositionProperty();
        }
    };

    public final static Field<GameObjectInstance, Property<AbsolutePositionAnchor>> ABSOLUTEPOSITIONANCHOR = new Field<GameObjectInstance, Property<AbsolutePositionAnchor>>("absolutePositionAnchor", Property.class) {
        @Override
        public Property<AbsolutePositionAnchor> getValue(GameObjectInstance aObject) {
            return aObject.absolutePositionAnchorProperty();
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