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

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSArrayReader;

// https://api.github.com/repos/mirkosertic/GameComposer/git/trees/a8ce3c2da3f0d1fc2a6c89af76c4de341fd55d7e?recursive=1
public abstract class GithubTree implements JSObject {

    interface TreeItem extends JSObject {
        @JSProperty
        String getPath();

        @JSProperty
        String getMode();

        @JSProperty
        String getType();

        @JSProperty
        String getSha();

        @JSProperty
        int getSize();

        @JSProperty
        String getUrl();
    }

    @JSProperty
    public abstract String getSha();

    @JSProperty
    public abstract JSArrayReader<TreeItem> getTree();
}