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

import de.mirkosertic.gameengine.web.Filesystem;
import org.teavm.jso.indexeddb.IDBDatabase;
import org.teavm.jso.indexeddb.IDBFactory;
import org.teavm.jso.indexeddb.IDBOpenDBRequest;

public class IndexedDBFilesystem implements Filesystem {

    public interface Callback {

        void onError();

        void onSuccess(IndexedDBFilesystem aFilesystem);
    }

    private static final String FILE_DATASTORE = "files";

    private final IDBDatabase database;

    public IndexedDBFilesystem(IDBDatabase aDatabase) {
        database = aDatabase;
    }

    public static void open(String aDatabaseName, Callback aCallback) {
        IDBFactory theFactory = IDBFactory.getInstance();
        IDBOpenDBRequest theRequest = theFactory.open(aDatabaseName, 1);
        theRequest.setOnUpgradeNeeded(aEvent -> theRequest.getResult().createObjectStore(FILE_DATASTORE));
        theRequest.setOnError(() -> aCallback.onError());
        theRequest.setOnSuccess(() -> aCallback.onSuccess(new IndexedDBFilesystem(theRequest.getResult())));
    }
}