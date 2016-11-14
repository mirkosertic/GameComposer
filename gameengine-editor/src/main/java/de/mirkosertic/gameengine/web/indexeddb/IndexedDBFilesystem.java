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

import java.util.HashMap;
import java.util.Map;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import de.mirkosertic.gameengine.web.Blob;
import de.mirkosertic.gameengine.web.File;
import de.mirkosertic.gameengine.web.FileReader;
import de.mirkosertic.gameengine.web.Filesystem;

public class IndexedDBFilesystem implements Filesystem {

    @JSBody(params = {"aObject"}, script = "return typeof aObject === 'undefined';")
    static native boolean isUndefined(JSObject aObject);

    private static final String FILE_DATASTORE = "files";

    private final IndexedDB database;
    private final Map<String, String> cachedURLs;

    private IndexedDBFilesystem(IndexedDB aDatabase) {
        database = aDatabase;
        cachedURLs = new HashMap<>();
    }

    public static Promise<IndexedDBFilesystem, String> open(String aDatabaseName) {
        return new Promise<>((aResolver, aRejector) -> {
            IndexedDBFactory theFactory = IndexedDBFactory.getFactory();
            IndexedDBOpenRequest theRequest = theFactory.open(aDatabaseName, 1);
            theRequest.setOnupgradeneeded(() -> theRequest.getResult().createObjectStore(FILE_DATASTORE));
            theRequest.setOnerror(() -> aRejector.reject("Error opening IndexedDB", null));
            theRequest.setOnsuccess(() -> aResolver.resolve(new IndexedDBFilesystem(theRequest.getResult())));
        });
    }

    @Override
    public Promise<File, String> openFile(String aFileName) {
        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> {
            IndexedDBTransaction theTransction = database.transaction(FILE_DATASTORE, "readonly");
            IndexedDBObjectStore theObjectStore = theTransction.objectStore(FILE_DATASTORE);
            IndexedDBGetRequest theRequest = theObjectStore.get(IndexedDBFile.createFileKey(aFileName));
            theRequest.setOnerror(() -> aRejector.reject("Error storing file " + aFileName, null));
            theRequest.setOnsuccess(() -> {
                if (isUndefined(theRequest.getResult())) {
                    aRejector.reject("File " + aFileName + " does not exist", null);
                } else {
                    aResolver.resolve(theRequest.getResult());
                }
            });
        });
    }

    @Override
    public Promise<File, String> storeFile(String aFileName, Blob aBlob) {
        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> {
            IndexedDBTransaction theTransction = database.transaction(FILE_DATASTORE, "readwrite");
            IndexedDBObjectStore theObjectStore = theTransction.objectStore(FILE_DATASTORE);
            IndexedDBFile theFile = IndexedDBFile.createCached(aFileName, aBlob);
            IndexedDBRequest theRequest = theObjectStore.put(theFile, IndexedDBFile.createFileKey(aFileName));
            theRequest.setOnerror(() -> aRejector.reject("Error storing file " + aFileName, null));
            theRequest.setOnsuccess(() -> aResolver.resolve(theFile));
        });
    }

    @Override
    public Promise<File, String> updateFile(String aFileName, Blob aBlob) {
        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> {
            IndexedDBTransaction theTransction = database.transaction(FILE_DATASTORE, "readwrite");
            IndexedDBObjectStore theObjectStore = theTransction.objectStore(FILE_DATASTORE);
            IndexedDBFile theFile = IndexedDBFile.createChanged(aFileName, aBlob);
            IndexedDBRequest theRequest = theObjectStore.put(theFile, IndexedDBFile.createFileKey(aFileName));
            theRequest.setOnerror(() -> aRejector.reject("Error storing file " + aFileName, null));
            theRequest.setOnsuccess(() -> aResolver.resolve(theFile));
        });
    }

    @Override
    public Promise<String, String> asDataURL(File aFile) {
        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> {
            String theCached = cachedURLs.get(aFile.getFilename());
            if (theCached != null) {
                aResolver.resolve(theCached);
            } else {
                FileReader theReader = FileReader.create();
                theReader.setOnload(() -> {
                    String theResult = theReader.getResult();
                    cachedURLs.put(aFile.getFilename(), theResult);
                    aResolver.resolve(theResult);
                });
                theReader.setOnerror(() -> TeaVMLogger.error("Error loading dataurl for " + aFile.getFilename()));
                theReader.readAsDataURL(aFile.getContent());
            }
        });
    }
}