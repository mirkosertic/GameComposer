package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class GameObjectInstanceClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.core.GameObjectInstance> FINDBEHAVIORBYTYPE = new Method<de.mirkosertic.gameengine.core.GameObjectInstance>("findBehaviorByType", new Class[] {String.class}) {
    @Override
    public Object invoke(GameObjectInstance aObject, Object[] aArguments) {
      return aObject.findBehaviorByType((String) aArguments[0]);
    }
  };

  public final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.String>> UUIDPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.String>>("uuidProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(GameObjectInstance aObject) {
      return aObject.uuidProperty();
    }
  };

  public final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Position>> POSITIONPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Position>>("positionProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Position> getValue(GameObjectInstance aObject) {
      return aObject.positionProperty();
    }
  };

  public final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.String>> NAMEPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.String>>("nameProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(GameObjectInstance aObject) {
      return aObject.nameProperty();
    }
  };

  public final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Angle>> ROTATIONANGLEPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Angle>>("rotationAngleProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Angle> getValue(GameObjectInstance aObject) {
      return aObject.rotationAngleProperty();
    }
  };

  public final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> VISIBLEPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("visibleProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(GameObjectInstance aObject) {
      return aObject.visibleProperty();
    }
  };

  public final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.PositionAnchor>> POSITIONANCHORPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.PositionAnchor>>("positionAnchorProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.PositionAnchor> getValue(GameObjectInstance aObject) {
      return aObject.positionAnchorProperty();
    }
  };

  public GameObjectInstanceClassInformation() {
    register(FINDBEHAVIORBYTYPE);
    register(UUIDPROPERTY);
    register(POSITIONPROPERTY);
    register(NAMEPROPERTY);
    register(ROTATIONANGLEPROPERTY);
    register(VISIBLEPROPERTY);
    register(POSITIONANCHORPROPERTY);
  }
}
