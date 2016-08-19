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

import de.mirkosertic.gameengine.teavm.TeaVMMap;
import org.teavm.jso.core.JSArray;

public class Repository {

    private final Github github;
    private final String user;
    private final String name;

    public Repository(Github aGithub, String aUser, String aName) {
        github = aGithub;
        user = aUser;
        name = aName;
    }

    public void visitContent(FileVisitor aVisitor) {
        github.open("/repos/" + user + "/" + name + "/contents", (aObject, aStatusCode, aResponseETag) -> {
            if (TeaVMMap.isArray(aObject)) {
                JSArray<File> theArray = aObject.cast();
                for (int i = 0; i < theArray.getLength(); i++) {
                    aVisitor.visit(theArray.get(i), aStatusCode, aResponseETag);
                }
            } else {
                aVisitor.visit((File) aObject, aStatusCode, aResponseETag);
            }
        }, true, null);
    }

    public void visitContent(File aFile, FileVisitor aVisitor, String aETag) {
        github.open(aFile.getUrl(), (aObject, aStatusCode, aResponseETag) -> {
            if (TeaVMMap.isArray(aObject)) {
                JSArray<File> theArray = aObject.cast();
                for (int i = 0; i < theArray.getLength(); i++) {
                    aVisitor.visit(theArray.get(i), aStatusCode, aResponseETag);
                }
            } else {
                aVisitor.visit((File) aObject, aStatusCode, aResponseETag);
            }
        }, false, aETag);
    }
}
