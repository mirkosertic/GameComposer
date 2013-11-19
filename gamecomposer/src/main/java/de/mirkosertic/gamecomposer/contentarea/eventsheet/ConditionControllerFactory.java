package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gameengine.core.Condition;
import de.mirkosertic.gameengine.core.GameScene;

public interface ConditionControllerFactory<T extends Controller,V extends Condition> {

    T createFor(GameScene aGameScene, V aCondition);
}
