package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface GameComponent {

    String TYPE_ATTRIBUTE = "type";

    Map<String, Object> serialize();
}
