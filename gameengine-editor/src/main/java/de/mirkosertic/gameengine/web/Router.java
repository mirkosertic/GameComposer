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

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.Window;
import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import org.teavm.jso.json.JSON;

import java.util.HashMap;
import java.util.Map;

public class Router {

    @JSBody(params = "aURI", script = "return encodeURI(aURI);")
    public static native String encodeURI(String aURI);

    @JSBody(params = "aURI", script = "return decodeURI(aURI);")
    public static native String decodeURI(String aURI);

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

    public <T extends JSObject> T getCurrentState() {
        String theCurrentLocation = window.getLocation().getFullURL();
        // Ignore hashes
        int p = theCurrentLocation.lastIndexOf("#");
        if (p>=0) {
            return (T) JSON.parse(decodeURI(theCurrentLocation.substring(p + 1)));
        }
        return null;
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
        // Ignore hashes
        int p = theCurrentLocation.lastIndexOf("#");
        if (p>=0) {
            theCurrentLocation = theCurrentLocation.substring(0, p);
        }
        TeaVMLogger.info("Current location is " + theCurrentLocation);
        for (Map.Entry<String, Route> theEntry : routes.entrySet()) {
            TeaVMLogger.info("Testing for route " + theEntry.getKey());
            if (theCurrentLocation.endsWith(theEntry.getKey())) {
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
        JSObject theCurrentState = getCurrentState();
        if (theCurrentState != null) {
            window.open(aURL + "#" + encodeURI(JSON.stringify(theCurrentState)), aTarget);
        } else {
            window.open(aURL, aTarget);
        }
    }

    public void openWithState(String aURL, JSObject aState) {
        String theState = encodeURI(JSON.stringify(aState));
        window.open(aURL + "#" + theState, "_self");
    }
}
