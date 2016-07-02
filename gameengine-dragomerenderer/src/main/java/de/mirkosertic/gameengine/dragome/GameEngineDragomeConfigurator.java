package de.mirkosertic.gameengine.dragome;

import java.net.URL;

import com.dragome.commons.DragomeConfiguratorImplementor;
import com.dragome.commons.compiler.CompilerMode;
import com.dragome.web.config.DomHandlerApplicationConfigurator;

@DragomeConfiguratorImplementor
public class GameEngineDragomeConfigurator extends DomHandlerApplicationConfigurator {

    public GameEngineDragomeConfigurator() {
        System.setProperty("dragome-compile-mode", CompilerMode.Debug.toString());
    }

    @Override
    public boolean filterClassPath(String aClassPathEntry) {
		boolean include= false;

		include|= aClassPathEntry.contains("gameengine-0.5");
		include|= aClassPathEntry.contains("gameengine-lua");
		include|= aClassPathEntry.contains("gameengine-jbox2d");
		include|= aClassPathEntry.contains("dragome-js-jre");
		include|= aClassPathEntry.contains("dragome-web");
		include|= aClassPathEntry.contains("dragome-w3c");
		include|= aClassPathEntry.contains("dragome-js-commons");
		include|= aClassPathEntry.contains("dragome-core");

		return include;
    }

    @Override
    public boolean isCheckingCast() {
        return false;
    }

    public boolean isRemoveUnusedCode()
    {
        return true;
    }
    
    public URL getAdditionalCodeKeepConfigFile() {
    	return getClass().getResource("/gameengine-proguard.conf");
    }
}