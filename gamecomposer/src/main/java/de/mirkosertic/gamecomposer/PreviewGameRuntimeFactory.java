package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.javafx.JDKBuiltInFunctions;
import de.mirkosertic.gameengine.type.Reflectable;

import javax.inject.Singleton;

@Singleton
public class PreviewGameRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    protected Reflectable createBuildInFunctions() {
        return new JDKBuiltInFunctions();
    }
}