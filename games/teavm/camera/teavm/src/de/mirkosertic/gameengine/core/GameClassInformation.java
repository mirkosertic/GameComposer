package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.*;

public class GameClassInformation extends ClassInformation {

  public static final Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.String>> NAMEPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.String>>("nameProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(Game aObject) {
      return aObject.nameProperty();
    }
  };

  public static final Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.String>> DEFAULTSCENEPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.String>>("defaultSceneProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(Game aObject) {
      return aObject.defaultSceneProperty();
    }
  };

  public static final Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> ENABLEWEBGLPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("enableWebGLProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(Game aObject) {
      return aObject.enableWebGLProperty();
    }
  };

  public static final Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> ENABLEDEBUGPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("enableDebugProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(Game aObject) {
      return aObject.enableDebugProperty();
    }
  };

  public static final Field<Game, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.CustomProperties>> CUSTOMPROPERTIESPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.CustomProperties>>("customPropertiesProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.CustomProperties> getValue(Game aObject) {
      return aObject.customPropertiesProperty();
    }
  };

  public GameClassInformation() {
    register(NAMEPROPERTY);
    register(DEFAULTSCENEPROPERTY);
    register(ENABLEWEBGLPROPERTY);
    register(ENABLEDEBUGPROPERTY);
    register(CUSTOMPROPERTIESPROPERTY);
  }
}
