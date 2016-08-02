package de.mirkosertic.gameengine.core;

public interface Logger {

    void info(String aMessage);

    void error(String aMessage);

    void time(String aLabel);

    void timeEnd(String aLabel);
}
