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

import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.web.AuthorizationState;
import de.mirkosertic.gameengine.web.EditorProject;
import de.mirkosertic.gameengine.web.ResourceAccessor;
import de.mirkosertic.gameengine.web.electron.fs.FS;
import de.mirkosertic.gameengine.web.html5.Blob;
import org.teavm.jso.core.JSString;
import org.teavm.jso.json.JSON;

public class LocalEditorProject implements EditorProject {

    private final FS fs;
    private final LocalProjectDefinition projectDefinition;

    public LocalEditorProject(FS aFS, LocalProjectDefinition aProjectDefinition) {
        projectDefinition = aProjectDefinition;
        fs = aFS;
    }

    @Override
    public Promise<ResourceAccessor, String> initializeResourceAccessor() {
        return new Promise<>((aResolver, aRejector) -> {
            LocalResourceAccessor theAccessor = new LocalResourceAccessor(fs, projectDefinition.getPath());
            theAccessor.persistFile(DEFINITION_FILENAME, Blob.createJSONBlob(JSString.valueOf(JSON.stringify(projectDefinition)))).thenContinue(aResult -> {
                aResolver.resolve(theAccessor);
            });
        });
    }

    @Override
    public boolean isAuthorizedWith(AuthorizationState aAuthorizationState) {
        return !aAuthorizationState.isNotLoggedIn();
    }
}