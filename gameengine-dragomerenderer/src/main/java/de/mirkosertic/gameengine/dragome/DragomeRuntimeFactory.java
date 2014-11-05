package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;

public class DragomeRuntimeFactory extends AbstractGameRuntimeFactory {

    @Override
    protected DragomeBuildInFunctions createBuildInFunctions() {
        return new DragomeBuildInFunctions();
    }
}
