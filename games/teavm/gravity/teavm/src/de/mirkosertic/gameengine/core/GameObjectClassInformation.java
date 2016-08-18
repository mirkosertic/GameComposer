package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class GameObjectClassInformation extends ClassInformation {

  public final Field<GameObject, de.mirkosertic.gameengine.event.Property<java.lang.String>> UUIDPROPERTY = new Field<GameObject, de.mirkosertic.gameengine.event.Property<java.lang.String>>("uuidProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(GameObject aObject) {
      return aObject.uuidProperty();
    }
  };

  public final Field<GameObject, de.mirkosertic.gameengine.event.Property<java.lang.String>> NAMEPROPERTY = new Field<GameObject, de.mirkosertic.gameengine.event.Property<java.lang.String>>("nameProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(GameObject aObject) {
      return aObject.nameProperty();
    }
  };

  public final Field<GameObject, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Size>> SIZEPROPERTY = new Field<GameObject, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Size>>("sizeProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Size> getValue(GameObject aObject) {
      return aObject.sizeProperty();
    }
  };

  public final Field<GameObject, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> VISIBLEPROPERTY = new Field<GameObject, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("visibleProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(GameObject aObject) {
      return aObject.visibleProperty();
    }
  };

  public GameObjectClassInformation() {
    register(UUIDPROPERTY);
    register(NAMEPROPERTY);
    register(SIZEPROPERTY);
    register(VISIBLEPROPERTY);
  }
}
