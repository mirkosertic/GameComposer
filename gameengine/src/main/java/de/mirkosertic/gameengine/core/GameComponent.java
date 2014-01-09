package de.mirkosertic.gameengine.core;

import java.util.Map;

public abstract class GameComponent {

    public static final String TYPE_ATTRIBUTE = "type";

    public abstract Map<String, Object> serialize();

    public abstract String getTypeKey();

    public abstract <T extends GameComponentTemplate> T getTemplate();
}
