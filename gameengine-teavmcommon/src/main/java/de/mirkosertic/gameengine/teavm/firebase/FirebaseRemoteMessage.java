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
package de.mirkosertic.gameengine.teavm.firebase;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class FirebaseRemoteMessage implements JSObject {

    @JSBody(
            params = {},
            script = "return new Object();"
    )
    public static native FirebaseRemoteMessage create();

    @JSIndexer
    public abstract void put(String aKey, JSObject aValue);

    @JSIndexer
    public abstract JSObject get(String aKey);

    @JSProperty(value = "keys")
    public abstract void setKeys(String[] aKeys);

    @JSProperty(value = "keys")
    public abstract String[] getKeys();
}
