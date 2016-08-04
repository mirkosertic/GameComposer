package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class SystemTickClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<SystemTick, Long> TOTALTICKS = new Field<SystemTick, Long>("totalTicks", Long.class) {
    @Override
    public Long getValue(SystemTick aObject) {
      return aObject.totalTicks;
    }
  };

  public final Field<SystemTick, Long> GAMETIME = new Field<SystemTick, Long>("gameTime", Long.class) {
    @Override
    public Long getValue(SystemTick aObject) {
      return aObject.gameTime;
    }
  };

  public final Field<SystemTick, Long> ELAPSEDTIMESINCELASTLOOP = new Field<SystemTick, Long>("elapsedTimeSinceLastLoop", Long.class) {
    @Override
    public Long getValue(SystemTick aObject) {
      return aObject.elapsedTimeSinceLastLoop;
    }
  };

  public SystemTickClassInformation() {
    register(TOTALTICKS);
    register(GAMETIME);
    register(ELAPSEDTIMESINCELASTLOOP);
  }
}
