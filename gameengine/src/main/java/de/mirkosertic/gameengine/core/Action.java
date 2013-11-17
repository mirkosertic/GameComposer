package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface Action {

    String TYPE_ATTRIBUTE = "type";

    void invoke(GameScene aScene);

    Map<String,Object> serialize();
}
