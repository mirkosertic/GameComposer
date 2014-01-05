package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.core.GameScene;

public interface ActionControllerFactory<T extends Controller,V extends Action> {

    T createFor(GameScene aGameScene, GameRule aRule, V anAction);
}
