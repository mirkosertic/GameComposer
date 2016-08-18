package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class PhysicsClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.physic.Physics> DELETE = new Method<de.mirkosertic.gameengine.physic.Physics>("delete", new Class[] {}) {
    @Override
    public Object invoke(Physics aObject, Object[] aArguments) {
      aObject.delete();
      return null;
    }
  };

  public final Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> ACTIVEPROPERTY = new Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("activeProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(Physics aObject) {
      return aObject.activeProperty();
    }
  };

  public final Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>> FIXEDROTATIONPROPERTY = new Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Boolean>>("fixedRotationProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Boolean> getValue(Physics aObject) {
      return aObject.fixedRotationProperty();
    }
  };

  public final Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Float>> DENSITYPROPERTY = new Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Float>>("densityProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Float> getValue(Physics aObject) {
      return aObject.densityProperty();
    }
  };

  public final Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Float>> FRICTIONPROPERTY = new Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Float>>("frictionProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Float> getValue(Physics aObject) {
      return aObject.frictionProperty();
    }
  };

  public final Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Float>> RESTITUTIONPROPERTY = new Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Float>>("restitutionProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Float> getValue(Physics aObject) {
      return aObject.restitutionProperty();
    }
  };

  public final Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Float>> GRAVITYSCALEPROPERTY = new Field<Physics, de.mirkosertic.gameengine.event.Property<java.lang.Float>>("gravityScaleProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Float> getValue(Physics aObject) {
      return aObject.gravityScaleProperty();
    }
  };

  public PhysicsClassInformation() {
    register(DELETE);
    register(ACTIVEPROPERTY);
    register(FIXEDROTATIONPROPERTY);
    register(DENSITYPROPERTY);
    register(FRICTIONPROPERTY);
    register(RESTITUTIONPROPERTY);
    register(GRAVITYSCALEPROPERTY);
  }
}
