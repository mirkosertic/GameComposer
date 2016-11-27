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
package de.mirkosertic.gameengine.web.electron;

import de.mirkosertic.gameengine.web.ProjectDefinition;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;

public abstract class LocalProjectDefinition implements ProjectDefinition {

    public static final String TYPE = "Local";

    @JSBody(params = {"aPath"}, script = "return {type: 'Locaö', path: aPath};")
    public native static LocalProjectDefinition create(String aPath);

    @JSProperty
    public abstract String getPath();
}