package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.DragomeConfiguratorImplementor;
import com.dragome.commons.compiler.CompilerMode;
import com.dragome.web.config.DomHandlerApplicationConfigurator;
import com.dragome.web.helpers.serverside.DefaultClasspathFilter;

import java.io.File;

@DragomeConfiguratorImplementor
public class GameEngineDragomeConfigurator extends DomHandlerApplicationConfigurator {

    public GameEngineDragomeConfigurator() {
        System.setProperty("dragome-compile-mode", CompilerMode.Production.toString());

        setClasspathFilter(new DefaultClasspathFilter() {
            @Override
            public boolean accept(File pathname, File aFolder) {
                boolean accept = super.accept(pathname, aFolder);

                String string = pathname.toString();

                accept &= !string.contains("java/util/concurrent");
                accept &= !string.contains("java/util/stream");
                accept &= !string.contains("java/util/function");
                accept &= !string.contains("java/sql");
                accept &= !string.contains("org/w3c/dom/html");
                accept &= !string.contains("java/org/junit");
                accept &= !string.contains("junit");

                return accept;
            }
        });
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
        if (aClassPathEntry.contains("dragome-w3c"))
            return true;
        if (aClassPathEntry.contains("dragome-web"))
            return true;
        if (aClassPathEntry.contains("dragome-js-commons"))
            return true;
        if (aClassPathEntry.contains("dragome-core"))
            return true;

        return false;
    }

    @Override
    public boolean isCheckingCast() {
        return false;
    }
}