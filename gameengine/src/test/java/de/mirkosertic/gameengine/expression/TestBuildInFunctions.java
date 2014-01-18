package de.mirkosertic.gameengine.expression;

public class TestBuildInFunctions extends BuiltInFunctions {

    @Override
    public String formatTime(Number aTimeInMilis, String aPattern) {
        throw new IllegalArgumentException();
    }
}
