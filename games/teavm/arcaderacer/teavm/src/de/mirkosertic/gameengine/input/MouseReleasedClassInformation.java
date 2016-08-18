package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class MouseReleasedClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<MouseReleased, de.mirkosertic.gameengine.type.Position> POSITION = new Field<MouseReleased, de.mirkosertic.gameengine.type.Position>("position", de.mirkosertic.gameengine.type.Position.class) {
    @Override
    public de.mirkosertic.gameengine.type.Position getValue(MouseReleased aObject) {
      return aObject.position;
    }
  };

  public final Field<MouseReleased, de.mirkosertic.gameengine.core.GameObjectInstance[]> AFFECTEDINSTANCES = new Field<MouseReleased, de.mirkosertic.gameengine.core.GameObjectInstance[]>("affectedInstances", de.mirkosertic.gameengine.core.GameObjectInstance[].class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObjectInstance[] getValue(MouseReleased aObject) {
      return aObject.affectedInstances;
    }
  };

  public MouseReleasedClassInformation() {
    register(POSITION);
    register(AFFECTEDINSTANCES);
  }
}
