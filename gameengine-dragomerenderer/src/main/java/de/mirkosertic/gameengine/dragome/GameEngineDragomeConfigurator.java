/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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