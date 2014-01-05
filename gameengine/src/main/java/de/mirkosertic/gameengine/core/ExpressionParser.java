package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.PropertyAware;
import de.mirkosertic.gameengine.type.ValueProvider;

public interface ExpressionParser {

    String SCENE_VARIABLE = "scene";
    String PLAYER_VARIABLE = "player";
    String CAMERA_VARIABLE = "camera";
    String INSTANCE_VARIABLE = "instance";
    String EVENT_PREFIX = "event.";

    String evaluateToString();

    void registerVariable(String aVariableName, PropertyAware aPropertyAware);

    <T> void registerVariable(String aVariableName, ValueProvider<T> aValueProvider);
}
