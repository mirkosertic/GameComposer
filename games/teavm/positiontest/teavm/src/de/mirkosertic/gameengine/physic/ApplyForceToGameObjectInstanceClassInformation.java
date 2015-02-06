package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.type.*;

public class ApplyForceToGameObjectInstanceClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public static final Field<ApplyForceToGameObjectInstance, de.mirkosertic.gameengine.core.GameObjectInstance> INSTANCE = new Field<ApplyForceToGameObjectInstance, de.mirkosertic.gameengine.core.GameObjectInstance>("instance", de.mirkosertic.gameengine.core.GameObjectInstance.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObjectInstance getValue(ApplyForceToGameObjectInstance aObject) {
      return aObject.instance;
    }
  };

  public static final Field<ApplyForceToGameObjectInstance, de.mirkosertic.gameengine.type.Force> FORCE = new Field<ApplyForceToGameObjectInstance, de.mirkosertic.gameengine.type.Force>("force", de.mirkosertic.gameengine.type.Force.class) {
    @Override
    public de.mirkosertic.gameengine.type.Force getValue(ApplyForceToGameObjectInstance aObject) {
      return aObject.force;
    }
  };

  public ApplyForceToGameObjectInstanceClassInformation() {
    register(INSTANCE);
    register(FORCE);
  }
}
