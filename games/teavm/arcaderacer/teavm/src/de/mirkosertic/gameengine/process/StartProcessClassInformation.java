package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class StartProcessClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<StartProcess, de.mirkosertic.gameengine.process.GameProcess> PROCESS = new Field<StartProcess, de.mirkosertic.gameengine.process.GameProcess>("process", de.mirkosertic.gameengine.process.GameProcess.class) {
    @Override
    public de.mirkosertic.gameengine.process.GameProcess getValue(StartProcess aObject) {
      return aObject.process;
    }
  };

  public StartProcessClassInformation() {
    register(PROCESS);
  }
}
