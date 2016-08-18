package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class CameraClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.camera.Camera> DELETE = new Method<de.mirkosertic.gameengine.camera.Camera>("delete", new Class[] {}) {
    @Override
    public Object invoke(Camera aObject, Object[] aArguments) {
      aObject.delete();
      return null;
    }
  };

  public final Field<Camera, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.camera.CameraType>> TYPEPROPERTY = new Field<Camera, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.camera.CameraType>>("typeProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.camera.CameraType> getValue(Camera aObject) {
      return aObject.typeProperty();
    }
  };

  public CameraClassInformation() {
    register(DELETE);
    register(TYPEPROPERTY);
  }
}
