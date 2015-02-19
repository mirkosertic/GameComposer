package de.mirkosertic.gameengine.network;

import de.mirkosertic.gameengine.core.GameScene;

import java.util.Map;

public interface EventInterpreter {

    void process(Map<String, Object> aEventData, GameScene aGameScene);
}
