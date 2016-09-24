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
package de.mirkosertic.gameengine.teavm.pixi;

import org.teavm.jso.*;

public abstract class Loader implements JSObject {

    public interface Resources extends JSObject {

        @JSIndexer
        Resource get(String aKey);
    }

    public interface Resource extends JSObject {
        @JSProperty
        JSObject getData();
    }

    public interface SpritesheetJSON extends JSObject {

        @JSProperty
        JSObject getFrames();
    }

    @JSFunctor
    public interface LoadHandler extends JSObject {
        void onLoad(Loader aLoader, Resources aResources);
    }

    @JSBody(params = {}, script = "return new PIXI.loaders.Loader();")
    public static native Loader create();

    public abstract void add(String aResourceName);

    public abstract void load(LoadHandler aHandler);
}