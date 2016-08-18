package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class ApplyForceToGameObjectInstanceClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<ApplyForceToGameObjectInstance, de.mirkosertic.gameengine.core.GameObjectInstance> INSTANCE = new Field<ApplyForceToGameObjectInstance, de.mirkosertic.gameengine.core.GameObjectInstance>("instance", de.mirkosertic.gameengine.core.GameObjectInstance.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObjectInstance getValue(ApplyForceToGameObjectInstance aObject) {
      return aObject.instance;
    }
  };

  public final Field<ApplyForceToGameObjectInstance, de.mirkosertic.gameengine.type.Force> FORCE = new Field<ApplyForceToGameObjectInstance, de.mirkosertic.gameengine.type.Force>("force", de.mirkosertic.gameengine.type.Force.class) {
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
