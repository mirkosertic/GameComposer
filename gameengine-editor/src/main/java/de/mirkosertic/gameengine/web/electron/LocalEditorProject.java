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

import de.mirkosertic.gameengine.web.EditorProject;
import de.mirkosertic.gameengine.web.electron.fs.FS;

public class LocalEditorProject implements EditorProject {

    private final FS fs;
    private final String localPath;

    public LocalEditorProject(FS aFS, String aLocalPath) {
        localPath = aLocalPath;
        fs = aFS;
    }

    public void initializeLoader(Callback aCallback) {
        aCallback.onSuccess(this, new LocalResourceAccessor(fs, localPath));
    }
}