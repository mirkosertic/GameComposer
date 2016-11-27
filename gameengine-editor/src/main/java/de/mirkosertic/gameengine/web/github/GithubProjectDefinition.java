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

import de.mirkosertic.gameengine.web.ProjectDefinition;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSProperty;

public abstract class GithubProjectDefinition implements ProjectDefinition {

    public static final String TYPE = "Github";

    @JSBody(params = {"aUser", "aRepository", "aRelativePath"}, script = "return {type: 'Github', user: aUser, repository: aRepository, relativePath: aRelativePath};")
    public native static GithubProjectDefinition create(String aUser, String aRepository, String aRelativePath);

    @JSProperty
    public abstract String getUser();

    @JSProperty
    public abstract String getRepository();

    @JSProperty
    public abstract String getRelativePath();
}
