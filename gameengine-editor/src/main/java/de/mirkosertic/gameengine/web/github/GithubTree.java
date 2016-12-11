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
package de.mirkosertic.gameengine.web.github;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArray;

// https://api.github.com/repos/mirkosertic/GameComposer/git/trees/a8ce3c2da3f0d1fc2a6c89af76c4de341fd55d7e?recursive=1
public abstract class GithubTree implements JSObject {

    @JSBody(params = {}, script = "return {};")
    public static native GithubTree create();

    public static abstract class TreeItem implements JSObject {

        @JSBody(params = {}, script = "return {};")
        public static native TreeItem create();

        @JSProperty
        public abstract String getPath();

        @JSProperty
        public abstract void setPath(String aPath);

        @JSProperty
        public abstract String getMode();

        @JSProperty
        public abstract void setMode(String aMode);

        @JSProperty
        public abstract String getType();

        @JSProperty
        public abstract void setType(String aType);

        @JSProperty
        public abstract String getSha();

        @JSProperty
        public abstract void setSha(String aSHA);

        @JSProperty
        public abstract int getSize();

        @JSProperty
        public abstract void setSize(int aSize);

        @JSBody(params = {}, script = "delete this.size;")
        public abstract void unsetSize();

        @JSProperty
        public abstract String getUrl();

        @JSBody(params = {}, script = "delete this.url;")
        public abstract void unsetUrl();
    }

    @JSProperty
    public abstract String getSha();

    @JSProperty
    public abstract JSArray<TreeItem> getTree();

    @JSProperty
    public abstract void setTree(JSArray<TreeItem> aTree);

    @JSProperty
    public abstract boolean isTruncated();

    @JSProperty
    public abstract void setBase_tree(String aBaseTreeHash);
}