package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.type.*;

public class DisableDynamicPhysicsClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public static final Field<DisableDynamicPhysics, de.mirkosertic.gameengine.core.GameObjectInstance> OBJECT = new Field<DisableDynamicPhysics, de.mirkosertic.gameengine.core.GameObjectInstance>("object", de.mirkosertic.gameengine.core.GameObjectInstance.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObjectInstance getValue(DisableDynamicPhysics aObject) {
      return aObject.object;
    }
  };

  public DisableDynamicPhysicsClassInformation() {
    register(OBJECT);
  }
}
