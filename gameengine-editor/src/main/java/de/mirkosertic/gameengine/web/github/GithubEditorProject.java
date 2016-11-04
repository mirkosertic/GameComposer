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

import de.mirkosertic.gameengine.web.EditorProject;
import de.mirkosertic.gameengine.web.indexeddb.IndexedDBFilesystem;

public class GithubEditorProject implements EditorProject {

    // http://mdswanson.com/blog/2011/07/23/digging-around-the-github-api-take-2.html
    // https://gist.github.com/robnyman/1894032

    private final String username;
    private final String repository;
    private final String relativePath;

    public GithubEditorProject(String aUsername, String aRepository, String aRelativePath) {
        username = aUsername;
        repository = aRepository;
        relativePath = aRelativePath;
    }

    public void initializeLoader(Callback aCallback) {
        IndexedDBFilesystem.open("github_" + username + "_" + repository, new IndexedDBFilesystem.Callback() {
            @Override
            public void onError() {
                aCallback.onError(GithubEditorProject.this);
            }

            @Override
            public void onSuccess(IndexedDBFilesystem aFilesystem) {
                String theBaseURL = "https://raw.githubusercontent.com/" + username + "/" + repository + "/master" + relativePath;
                aCallback.onSuccess(GithubEditorProject.this, new GithubResourceLoaderFactory(theBaseURL, aFilesystem));
            }
        });
    }
}