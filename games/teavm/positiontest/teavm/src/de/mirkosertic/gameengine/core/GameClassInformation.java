package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class GameClassInformation extends ClassInformation {

  public final Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.String>> NAMEPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.String>>("nameProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(Game aObject) {
      return aObject.nameProperty();
    }
  };

  public final Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.String>> DEFAULTSCENEPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.String>>("defaultSceneProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(Game aObject) {
      return aObject.defaultSceneProperty();
    }
  };

  public final Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> ENABLEWEBGLPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("enableWebGLProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(Game aObject) {
      return aObject.enableWebGLProperty();
    }
  };

  public final Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> ENABLEDEBUGPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("enableDebugProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(Game aObject) {
      return aObject.enableDebugProperty();
    }
  };

  public final Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> ENABLENETWORKINGPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("enableNetworkingProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(Game aObject) {
      return aObject.enableNetworkingProperty();
    }
  };

  public final Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.String>> FIREBASEURLPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<java.lang.String>>("fireBaseURLProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(Game aObject) {
      return aObject.fireBaseURLProperty();
    }
  };

  public final Field<Game, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.CustomProperties>> CUSTOMPROPERTIESPROPERTY = new Field<Game, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.CustomProperties>>("customPropertiesProperty", de.mirkosertic.gameengine.event.Property.class) {
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
    register(ENABLENETWORKINGPROPERTY);
    register(FIREBASEURLPROPERTY);
    register(CUSTOMPROPERTIESPROPERTY);
  }
}
