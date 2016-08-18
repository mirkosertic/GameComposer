package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class ConstantMovementClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.arcade.ConstantMovement> DELETE = new Method<de.mirkosertic.gameengine.arcade.ConstantMovement>("delete", new Class[] {}) {
    @Override
    public Object invoke(ConstantMovement aObject, Object[] aArguments) {
      aObject.delete();
      return null;
    }
  };

  public final Field<ConstantMovement, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Speed>> SPEEDPROPERTY = new Field<ConstantMovement, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Speed>>("speedProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Speed> getValue(ConstantMovement aObject) {
      return aObject.speedProperty();
    }
  };

  public final Field<ConstantMovement, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Speed>> ROTATIONSPEEDPROPERTY = new Field<ConstantMovement, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Speed>>("rotationSpeedProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Speed> getValue(ConstantMovement aObject) {
      return aObject.rotationSpeedProperty();
    }
  };

  public ConstantMovementClassInformation() {
    register(DELETE);
    register(SPEEDPROPERTY);
    register(ROTATIONSPEEDPROPERTY);
  }
}
