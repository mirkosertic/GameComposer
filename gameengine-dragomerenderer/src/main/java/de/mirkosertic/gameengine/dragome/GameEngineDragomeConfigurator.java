package de.mirkosertic.gameengine.dragome;

import java.net.URL;
import java.util.Arrays;

import org.w3c.dom.events.KeyboardEvent;
import org.w3c.dom.events.MouseEvent;

import com.dragome.commons.DragomeConfiguratorImplementor;
import com.dragome.commons.compiler.CompilerMode;
import com.dragome.web.config.DomHandlerApplicationConfigurator;

@DragomeConfiguratorImplementor
public class GameEngineDragomeConfigurator extends DomHandlerApplicationConfigurator {

    public GameEngineDragomeConfigurator() {
    	   super(Arrays.asList(MouseEvent.class, KeyboardEvent.class));
        System.setProperty("dragome-compile-mode", CompilerMode.Debug.toString());
    }

    @Override
    public boolean filterClassPath(String aClassPathEntry) {
		boolean include= false;

		include|= aClassPathEntry.contains("gameengine-");
		include|= aClassPathEntry.contains("dragome-js-jre");
		include|= aClassPathEntry.contains("dragome-web");
		include|= aClassPathEntry.contains("dragome-w3c");
		include|= aClassPathEntry.contains("dragome-js-commons");
		include|= aClassPathEntry.contains("dragome-core");
        include&= !aClassPathEntry.contains("gameengine-apt");
		return include;
    }

    @Override
    public boolean isCheckingCast() {
        return false;
    }

    public boolean isRemoveUnusedCode() {
        return false;
    }
    
    public URL getAdditionalCodeKeepConfigFile() {
    	return getClass().getResource("/gameengine-proguard.conf");
    }
}