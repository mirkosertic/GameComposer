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
package de.mirkosertic.gameengine.web;

import org.teavm.jso.browser.Window;

import java.util.HashMap;
import java.util.Map;

public class Router {

    public interface Route {

        void renderContent(Window aWindow);
    }

    private final Map<String, Route> routes;
    private final Window window;
    private String defaultPath;

    public Router(Window aWindow) {
        window = aWindow;
        routes = new HashMap<>();
    }

    public Router add(String aPath, Route aRoute) {
        routes.put(aPath, aRoute);
        return this;
    }

    public Router widhDefaultPath(String aPath) {
        defaultPath = aPath;
        return this;
    }

    public void handleNavigation() {
        String theCurrentLocation = window.getLocation().getFullURL();
        for (Map.Entry<String, Route> theEntry : routes.entrySet()) {
            if (theCurrentLocation.contains(theEntry.getKey())) {
                theEntry.getValue().renderContent(window);
                return;
            }
        }
        if (defaultPath != null) {
            Route theRoute = routes.get(defaultPath);
            if (theRoute != null) {
                theRoute.renderContent(window);
                return;
            }
        }
        throw new IllegalStateException("No matching route found");
    }

    public void open(String aURL, String aTarget) {
        window.open(aURL, aTarget);
    }
}
