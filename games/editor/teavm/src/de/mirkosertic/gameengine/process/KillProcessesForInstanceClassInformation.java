package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class KillProcessesForInstanceClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<KillProcessesForInstance, de.mirkosertic.gameengine.core.GameObjectInstance> INSTANCE = new Field<KillProcessesForInstance, de.mirkosertic.gameengine.core.GameObjectInstance>("instance", de.mirkosertic.gameengine.core.GameObjectInstance.class) {
    @Override
    public de.mirkosertic.gameengine.core.GameObjectInstance getValue(KillProcessesForInstance aObject) {
      return aObject.instance;
    }
  };

  public KillProcessesForInstanceClassInformation() {
    register(INSTANCE);
  }
}
