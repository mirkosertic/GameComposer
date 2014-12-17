package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.ChainedInstrumentationDragomeConfigurator;
import com.dragome.commons.DragomeConfiguratorImplementor;

@DragomeConfiguratorImplementor
public class GameEngineDragomeConfigurator extends ChainedInstrumentationDragomeConfigurator {

    public GameEngineDragomeConfigurator() {
    }

    @Override
    public boolean filterClassPath(String aClassPathEntry) {
        if (aClassPathEntry.contains("gameengine-0.3")) {
            return true;
        }
        if (aClassPathEntry.contains("jbox2d-")) {
            return true;
        }
        if (aClassPathEntry.contains("dragome-js-jre")) {
            return true;
        }
        if (aClassPathEntry.contains("dragome-js-commons")) {
            return true;
        }
        if (aClassPathEntry.contains("dragome-core")) {
            return true;
        }
        if (aClassPathEntry.contains("dragome-guia")) {
            return true;
        }
        if (aClassPathEntry.contains("dragome-form-bindings")) {
            return true;
        }
        if (aClassPathEntry.contains("dragome-method-logger")) {
            return true;
        }
        return false;
    }
}
