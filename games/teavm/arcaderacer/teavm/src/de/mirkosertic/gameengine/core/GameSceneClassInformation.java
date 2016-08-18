package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class GameSceneClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.core.GameScene> ADDINSTANCE = new Method<de.mirkosertic.gameengine.core.GameScene>("addInstance", new Class[] {de.mirkosertic.gameengine.core.GameObjectInstance.class}) {
    @Override
    public Object invoke(GameScene aObject, Object[] aArguments) {
      aObject.addInstance((de.mirkosertic.gameengine.core.GameObjectInstance) aArguments[0]);
      return null;
    }
  };

  public final Method<de.mirkosertic.gameengine.core.GameScene> FINDOBJECTBYNAME = new Method<de.mirkosertic.gameengine.core.GameScene>("findObjectByName", new Class[] {String.class}) {
    @Override
    public Object invoke(GameScene aObject, Object[] aArguments) {
      return aObject.findObjectByName((String) aArguments[0]);
    }
  };

  public final Method<de.mirkosertic.gameengine.core.GameScene> FINDINSTANCEBYNAME = new Method<de.mirkosertic.gameengine.core.GameScene>("findInstanceByName", new Class[] {String.class}) {
    @Override
    public Object invoke(GameScene aObject, Object[] aArguments) {
      return aObject.findInstanceByName((String) aArguments[0]);
    }
  };

  public final Method<de.mirkosertic.gameengine.core.GameScene> FINDINSTANCEBYID = new Method<de.mirkosertic.gameengine.core.GameScene>("findInstanceByID", new Class[] {String.class}) {
    @Override
    public Object invoke(GameScene aObject, Object[] aArguments) {
      return aObject.findInstanceByID((String) aArguments[0]);
    }
  };

  public final Method<de.mirkosertic.gameengine.core.GameScene> CREATEFROM = new Method<de.mirkosertic.gameengine.core.GameScene>("createFrom", new Class[] {de.mirkosertic.gameengine.core.GameObject.class}) {
    @Override
    public Object invoke(GameScene aObject, Object[] aArguments) {
      return aObject.createFrom((de.mirkosertic.gameengine.core.GameObject) aArguments[0]);
    }
  };

  public final Method<de.mirkosertic.gameengine.core.GameScene> REMOVEGAMEOBJECTINSTANCE = new Method<de.mirkosertic.gameengine.core.GameScene>("removeGameObjectInstance", new Class[] {de.mirkosertic.gameengine.core.GameObjectInstance.class}) {
    @Override
    public Object invoke(GameScene aObject, Object[] aArguments) {
      aObject.removeGameObjectInstance((de.mirkosertic.gameengine.core.GameObjectInstance) aArguments[0]);
      return null;
    }
  };

  public final Field<GameScene, de.mirkosertic.gameengine.event.Property<java.lang.String>> NAMEPROPERTY = new Field<GameScene, de.mirkosertic.gameengine.event.Property<java.lang.String>>("nameProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.String> getValue(GameScene aObject) {
      return aObject.nameProperty();
    }
  };

  public final Field<GameScene, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.core.GameObject>> CAMERAOBJECTPROPERTY = new Field<GameScene, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.core.GameObject>>("cameraObjectProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.core.GameObject> getValue(GameScene aObject) {
      return aObject.cameraObjectProperty();
    }
  };

  public final Field<GameScene, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Color>> BACKGROUNDCOLORPROPERTY = new Field<GameScene, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Color>>("backgroundColorProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Color> getValue(GameScene aObject) {
      return aObject.backgroundColorProperty();
    }
  };

  public final Field<GameScene, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.core.GameObject>> DEFAULTPLAYERPROPERTY = new Field<GameScene, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.core.GameObject>>("defaultPlayerProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.core.GameObject> getValue(GameScene aObject) {
      return aObject.defaultPlayerProperty();
    }
  };

  public final Field<GameScene, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Rectangle>> LAYOUTBOUNDSPROPERTY = new Field<GameScene, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Rectangle>>("layoutBoundsProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Rectangle> getValue(GameScene aObject) {
      return aObject.layoutBoundsProperty();
    }
  };

  public final Field<GameScene, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.CustomProperties>> CUSTOMPROPERTIESPROPERTY = new Field<GameScene, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.CustomProperties>>("customPropertiesProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.CustomProperties> getValue(GameScene aObject) {
      return aObject.customPropertiesProperty();
    }
  };

  public GameSceneClassInformation() {
    register(ADDINSTANCE);
    register(FINDOBJECTBYNAME);
    register(FINDINSTANCEBYNAME);
    register(FINDINSTANCEBYID);
    register(CREATEFROM);
    register(REMOVEGAMEOBJECTINSTANCE);
    register(NAMEPROPERTY);
    register(CAMERAOBJECTPROPERTY);
    register(BACKGROUNDCOLORPROPERTY);
    register(DEFAULTPLAYERPROPERTY);
    register(LAYOUTBOUNDSPROPERTY);
    register(CUSTOMPROPERTIESPROPERTY);
  }
}
