package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.core.GameRuntime;

public interface GameProcessFactory<T extends GameProcess> {

    T create(GameRuntime aGameRuntime);
}
