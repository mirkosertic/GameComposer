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

import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import de.mirkosertic.gameengine.web.Blob;
import de.mirkosertic.gameengine.web.File;
import de.mirkosertic.gameengine.web.FileReader;
import de.mirkosertic.gameengine.web.Filesystem;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import java.util.HashMap;
import java.util.Map;

public class IndexedDBFilesystem implements Filesystem {

    @JSBody(params = {"aObject"}, script = "return typeof aObject === 'undefined';")
    static native boolean isUndefined(JSObject aObject);

    public interface Callback {

        void onError();

        void onSuccess(IndexedDBFilesystem aFilesystem);
    }

    private static final String FILE_DATASTORE = "files";

    private final IndexedDB database;
    private final Map<String, String> cachedURLs;

    private IndexedDBFilesystem(IndexedDB aDatabase) {
        database = aDatabase;
        cachedURLs = new HashMap<>();
    }

    public static void open(String aDatabaseName, Callback aCallback) {
        IndexedDBFactory theFactory = IndexedDBFactory.getFactory();
        IndexedDBOpenRequest theRequest = theFactory.open(aDatabaseName, 1);
        theRequest.setOnupgradeneeded(() -> theRequest.getResult().createObjectStore(FILE_DATASTORE));
        theRequest.setOnerror(() -> aCallback.onError());
        theRequest.setOnsuccess(() -> aCallback.onSuccess(new IndexedDBFilesystem(theRequest.getResult())));
    }

    @Override
    public void openFile(String aFileName, FileProcessor aProcessor) {
        IndexedDBTransaction theTransction = database.transaction(FILE_DATASTORE, "readonly");
        IndexedDBObjectStore theObjectStore = theTransction.objectStore(FILE_DATASTORE);
        IndexedDBGetRequest theRequest = theObjectStore.get(IndexedDBFile.createFileKey(aFileName));
        theRequest.setOnerror(() -> TeaVMLogger.error("Error storing file " + aFileName));
        theRequest.setOnsuccess(() -> {
            if (isUndefined(theRequest.getResult())) {
                aProcessor.doesNotExist(aFileName);
            } else {
                aProcessor.process((IndexedDBFile) theRequest.getResult());
            }
        });
    }

    @Override
    public void storeFile(String aFileName, Blob aBlob, FileProcessor aProcessor) {

        IndexedDBTransaction theTransction = database.transaction(FILE_DATASTORE, "readwrite");
        IndexedDBObjectStore theObjectStore = theTransction.objectStore(FILE_DATASTORE);
        IndexedDBFile theFile = IndexedDBFile.createCached(aFileName, aBlob);
        IndexedDBRequest theRequest = theObjectStore.put(theFile, IndexedDBFile.createFileKey(aFileName));
        theRequest.setOnerror(() -> TeaVMLogger.error("Error storing file " + aFileName));
        theRequest.setOnsuccess(() -> aProcessor.process(theFile));
    }

    @Override
    public void asDataURL(File aFile, DataUrlCallback aCallback) {
        String theCached = cachedURLs.get(aFile.getFilename());
        if (theCached != null) {
            aCallback.handle(theCached);
        } else {
            FileReader theReader = FileReader.create();
            theReader.setOnload(() -> {
                String theResult = theReader.getResult();
                cachedURLs.put(aFile.getFilename(), theResult);
                aCallback.handle(theResult);
            });
            theReader.setOnerror(() -> TeaVMLogger.error("Error loading dataurl for " + aFile.getFilename()));
            theReader.readAsDataURL(aFile.getContent());
        }
    }
}