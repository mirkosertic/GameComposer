package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ValueProvider;

public interface ExpressionParser {

    String SCENE_VARIABLE = "scene";
    String PLAYER_VARIABLE = "player";
    String CAMERA_VARIABLE = "camera";
    String INSTANCE_VARIABLE = "instance";
    String EVENT_VARIABLE = "event";
    String OLD_VALUE_VARIABLE = "oldvalue";

    Object evaluateToObject();

    String evaluateToString();

    <T> void registerVariable(String aVariableName, ValueProvider<T> aVariableValueProvider);

    <T> void registerVariable(String aVariableName, T aVariable);
}
