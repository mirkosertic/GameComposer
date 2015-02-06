package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.type.*;

public class EnableDynamicPhysicsClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public static final Field<EnableDynamicPhysics, de.mirkosertic.gameengine.core.GameObjectInstance> OBJECT = new Field<EnableDynamicPhysics, de.mirkosertic.gameengine.core.GameObjectInstance>("object", de.mirkosertic.gameengine.core.GameObjectInstance.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObjectInstance getValue(EnableDynamicPhysics aObject) {
      return aObject.object;
    }
  };

  public EnableDynamicPhysicsClassInformation() {
    register(OBJECT);
  }
}
