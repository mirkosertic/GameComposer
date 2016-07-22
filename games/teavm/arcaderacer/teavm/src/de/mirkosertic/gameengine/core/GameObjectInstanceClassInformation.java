package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.*;

public class GameObjectInstanceClassInformation extends ClassInformation {

  public static final Method<de.mirkosertic.gameengine.core.GameObjectInstance> FINDBEHAVIORBYTYPE = new Method<de.mirkosertic.gameengine.core.GameObjectInstance>("findBehaviorByType", de.mirkosertic.gameengine.core.Behavior.class, new Class[] {String.class}) {
    @Override
    public Object invoke(GameObjectInstance aObject, Object[] aArguments) {
      return aObject.findBehaviorByType((String) aArguments[0]);
    }
  };

  public static final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.String>> UUIDPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.String>>("uuidProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(GameObjectInstance aObject) {
      return aObject.uuidProperty();
    }
  };

  public static final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Position>> POSITIONPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Position>>("positionProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Position> getValue(GameObjectInstance aObject) {
      return aObject.positionProperty();
    }
  };

  public static final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.String>> NAMEPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.String>>("nameProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(GameObjectInstance aObject) {
      return aObject.nameProperty();
    }
  };

  public static final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Angle>> ROTATIONANGLEPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Angle>>("rotationAngleProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Angle> getValue(GameObjectInstance aObject) {
      return aObject.rotationAngleProperty();
    }
  };

  public static final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> VISIBLEPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("visibleProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(GameObjectInstance aObject) {
      return aObject.visibleProperty();
    }
  };

  public static final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> ABSOLUTEPOSITIONPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("absolutePositionProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(GameObjectInstance aObject) {
      return aObject.absolutePositionProperty();
    }
  };

  public static final Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.AbsolutePositionAnchor>> ABSOLUTEPOSITIONANCHORPROPERTY = new Field<GameObjectInstance, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.AbsolutePositionAnchor>>("absolutePositionAnchorProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.AbsolutePositionAnchor> getValue(GameObjectInstance aObject) {
      return aObject.absolutePositionAnchorProperty();
    }
  };

  public GameObjectInstanceClassInformation() {
    register(FINDBEHAVIORBYTYPE);
    register(UUIDPROPERTY);
    register(POSITIONPROPERTY);
    register(NAMEPROPERTY);
    register(ROTATIONANGLEPROPERTY);
    register(VISIBLEPROPERTY);
    register(ABSOLUTEPOSITIONPROPERTY);
    register(ABSOLUTEPOSITIONANCHORPROPERTY);
  }
}
