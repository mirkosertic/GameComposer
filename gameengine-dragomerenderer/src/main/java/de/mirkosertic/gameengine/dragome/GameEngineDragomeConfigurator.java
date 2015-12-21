package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.DragomeConfiguratorImplementor;
import com.dragome.commons.compiler.CompilerMode;
import com.dragome.web.config.DomHandlerApplicationConfigurator;

@DragomeConfiguratorImplementor
public class GameEngineDragomeConfigurator extends DomHandlerApplicationConfigurator {

    public GameEngineDragomeConfigurator() {
        System.setProperty("dragome-compile-mode", CompilerMode.Production.toString());
    }

    @Override
    public boolean filterClassPath(String aClassPathEntry) {
        if (aClassPathEntry.contains("gameengine-0.5"))
            return true;
        if (aClassPathEntry.contains("gameengine-lua"))
            return true;
        if (aClassPathEntry.contains("gameengine-jbox2d"))
            return true;
        if (aClassPathEntry.contains("dragome-js-jre"))
            return true;
        if (aClassPathEntry.contains("dragome-js-commons"))
            return true;
        if (aClassPathEntry.contains("dragome-core"))
            return true;
        if (aClassPathEntry.contains("dragome-web"))
            return true;

        return false;
    }

    @Override
    public boolean isCheckingCast() {
        return false;
    }
}