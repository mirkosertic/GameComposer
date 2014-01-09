package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Rectangle;

public class GameSceneClassInformation extends ClassInformation {

    public static final GameSceneClassInformation INSTANCE = new GameSceneClassInformation();

    public final static Field<GameScene, Property<String>> NAME = new Field<GameScene, Property<String>>("name", Property.class) {
        @Override
        public Property<String> getValue(GameScene aObject) {
            return aObject.name;
        }
    };

    public final static Field<GameScene, Property<GameObject>> CAMERAOBJECT = new Field<GameScene, Property<GameObject>>("cameraObject", Property.class) {
        @Override
        public Property<GameObject> getValue(GameScene aObject) {
            return aObject.cameraObject;
        }
    };

    public final static Field<GameScene, Property<GameObject>> DEFAULTPLAYER = new Field<GameScene, Property<GameObject>>("defaultPlayer", Property.class) {
        @Override
        public Property<GameObject> getValue(GameScene aObject) {
            return aObject.defaultPlayer;
        }
    };

    public final static Field<GameScene, Property<Color>> BACKGROUNDCOLOR = new Field<GameScene, Property<Color>>("backgroundColor", Property.class) {
        @Override
        public Property<Color> getValue(GameScene aObject) {
            return aObject.backgroundColor;
        }
    };

    public final static Field<GameScene, Property<Rectangle>> LAYOUTBOUNDS = new Field<GameScene, Property<Rectangle>>("layoutBounds", Property.class) {
        @Override
        public Property<Rectangle> getValue(GameScene aObject) {
            return aObject.layoutBounds;
        }
    };

    private GameSceneClassInformation() {
        register(NAME);
        register(CAMERAOBJECT);
        register(DEFAULTPLAYER);
        register(BACKGROUNDCOLOR);
        register(LAYOUTBOUNDS);
    }
}
