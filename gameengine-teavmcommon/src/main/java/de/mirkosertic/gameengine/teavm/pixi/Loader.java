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

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;

public abstract class Loader extends EventEmitter {

    public interface Resources extends JSObject {

        @JSIndexer
        LoaderResource get(String aKey);
    }

    @JSFunctor
    public interface LoadHandler extends JSObject {
        void onLoad(Loader aLoader, Resources aResources);
    }

    @JSBody(params = {}, script = "return new PIXI.loaders.Loader();")
    public static native Loader create();

    public abstract Loader pre(LoaderMiddleware aMiddleware);

    public abstract void add(String aResourceName);

    public abstract void load(LoadHandler aHandler);
}