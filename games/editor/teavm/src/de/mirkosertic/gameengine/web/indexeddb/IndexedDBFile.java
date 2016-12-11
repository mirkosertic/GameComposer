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
package de.mirkosertic.gameengine.web.indexeddb;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import de.mirkosertic.gameengine.web.html5.Blob;
import de.mirkosertic.gameengine.web.html5.File;

public abstract class IndexedDBFile implements File {

    public static JSObject createFileKey(String aFileName) {
        return JSString.valueOf(aFileName);
    }

    @JSBody(params = {"aFileName", "aBlob"}, script = "return {filename: aFileName, content: aBlob, status: 'cached'};")
    public static native IndexedDBFile createCached(String aFileName, Blob aBlob);

    @JSBody(params = {"aFileName", "aBlob"}, script = "return {filename: aFileName, content: aBlob, status: 'changed'};")
    public static native IndexedDBFile createChanged(String aFileName, Blob aBlob);
}
