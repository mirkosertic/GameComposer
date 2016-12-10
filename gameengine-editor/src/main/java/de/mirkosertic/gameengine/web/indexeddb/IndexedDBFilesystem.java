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

import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.core.PromiseRejector;
import de.mirkosertic.gameengine.core.PromiseResolver;
import de.mirkosertic.gameengine.teavm.TeaVMLogger;
import de.mirkosertic.gameengine.web.Filesystem;
import de.mirkosertic.gameengine.web.html5.Blob;
import de.mirkosertic.gameengine.web.html5.File;
import de.mirkosertic.gameengine.web.html5.FileReader;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IndexedDBFilesystem implements Filesystem {

    @JSBody(params = {"aObject"}, script = "return typeof aObject === 'undefined';")
    static native boolean isUndefined(JSObject aObject);

    private static final String ADMIN_DATASTORE = "admin";
    private static final String FILE_DATASTORE = "files";

    private final IndexedDB database;
    private final Map<String, String> cachedURLs;

    private IndexedDBFilesystem(IndexedDB aDatabase) {
        database = aDatabase;
        cachedURLs = new HashMap<>();
    }

    public static Promise<IndexedDB, String> getAdminDatabase() {
        return new Promise<>((aResolver, aRejector) -> {
            IndexedDBFactory theFactory = IndexedDBFactory.getFactory();
            IndexedDBOpenRequest theGetAdminRequest = theFactory.open("GameComposerAdminData", 1);
            theGetAdminRequest.setOnupgradeneeded((aUpgradeEvent) -> theGetAdminRequest.getResult().createObjectStore(ADMIN_DATASTORE));
            theGetAdminRequest.setOnerror((aErrorEvent) -> aRejector.reject("Error accessing admin data", null));
            theGetAdminRequest.setOnsuccess((aSuccessEvent) -> aResolver.resolve(theGetAdminRequest.getResult()));
        });
    }

    public static Promise<String[], String> listDatabases() {
        return new Promise<>((aResolver, aRejector) -> {
            Promise<IndexedDB, String> theAdminDB = getAdminDatabase();
            theAdminDB.catchError((aResult, aOptionalException) -> aRejector.reject(aRejector, aOptionalException));
            theAdminDB.thenContinue(aResult -> {
                final Set<String> theDatabases = new HashSet<>();
                IndexedDBObjectStore theObjectStore = aResult.transaction(ADMIN_DATASTORE, "readonly").objectStore(ADMIN_DATASTORE);
                theObjectStore.openCursor().setOnsuccess(aEvent -> {
                    IndexedDBCursor theCursor = aEvent.getTarget().getResult();
                    if (theCursor == null) {
                        // Finished with iteration)
                        aResolver.resolve(theDatabases.toArray(new String[theDatabases.size()]));
                    } else {
                        JSString theKey = theCursor.getKey();
                        theDatabases.add(theKey.stringValue());
                        theCursor.continueWithCursor();
                    }
                });
            });
        });
    }

    public static Promise<IndexedDBFilesystem, String> open(String aDatabaseName) {
        return new Promise<>((aResolver, aRejector) -> {

            Promise<IndexedDB, String> theAdminRequest = getAdminDatabase();
            theAdminRequest.catchError((aResult, aOptionalException) -> aRejector.reject(aRejector, aOptionalException));
            theAdminRequest.thenContinue(aAdminDB -> {

                IndexedDBFactory theFactory = IndexedDBFactory.getFactory();

                IndexedDBTransaction theTransction = aAdminDB.transaction(ADMIN_DATASTORE, "readwrite");
                IndexedDBObjectStore theObjectStore = theTransction.objectStore(ADMIN_DATASTORE);
                IndexedDBGetRequest theGetAdminMarkerRequest = theObjectStore.get(JSString.valueOf(aDatabaseName));
                theGetAdminMarkerRequest.setOnsuccess((aAdminSuccessEvent) -> {
                    if (isUndefined(theGetAdminMarkerRequest.getResult())) {
                        IndexedDBRequest thePutAdminMarkerRequest = theObjectStore.put(JSString.valueOf("opened"), JSString.valueOf(aDatabaseName));
                        thePutAdminMarkerRequest.setOnsuccess((aPutAdminMarkerSuccessEvent) -> {
                            IndexedDBOpenRequest theOpenDatabaseRequest = theFactory.open(aDatabaseName, 1);
                            theOpenDatabaseRequest.setOnupgradeneeded((aUpgradeNeededEvent) -> theOpenDatabaseRequest.getResult().createObjectStore(FILE_DATASTORE));
                            theOpenDatabaseRequest.setOnerror((aErrorEvent) -> aRejector.reject("Error opening IndexedDB", null));
                            theOpenDatabaseRequest.setOnsuccess((aOpenDatabaseSuccessEvent) -> aResolver.resolve(new IndexedDBFilesystem(theOpenDatabaseRequest.getResult())));
                        });
                        thePutAdminMarkerRequest.setOnerror((aErrorEvent) -> aRejector.reject("Error saving admin data", null));
                    } else {
                        // Datenbank existiert, also weiter mit öffnen
                        IndexedDBOpenRequest theOpenDatabaseRequest = theFactory.open(aDatabaseName, 1);
                        theOpenDatabaseRequest.setOnupgradeneeded((aUpgradeNeededEvent) -> theOpenDatabaseRequest.getResult().createObjectStore(FILE_DATASTORE));
                        theOpenDatabaseRequest.setOnerror((aErrorEvent) -> aRejector.reject("Error opening IndexedDB", null));
                        theOpenDatabaseRequest.setOnsuccess((aOpenDatabaseSuccessEvent) -> aResolver.resolve(new IndexedDBFilesystem(theOpenDatabaseRequest.getResult())));
                    }
                });
                theGetAdminMarkerRequest.setOnerror((aErrorEvent) -> aRejector.reject("Error saving admin data", null));
            });
        });
    }

    @Override
    public Promise<File, String> openFile(String aFileName) {
        return new Promise<>((aResolver, aRejector) -> {
            IndexedDBTransaction theTransction = database.transaction(FILE_DATASTORE, "readonly");
            IndexedDBObjectStore theObjectStore = theTransction.objectStore(FILE_DATASTORE);
            IndexedDBGetRequest theRequest = theObjectStore.get(IndexedDBFile.createFileKey(aFileName));
            theRequest.setOnerror((aErrorEvent) -> aRejector.reject("Error storing file " + aFileName, null));
            theRequest.setOnsuccess((aSuccessEvent) -> {
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
        return new Promise<>((aResolver, aRejector) -> {
            IndexedDBTransaction theTransction = database.transaction(FILE_DATASTORE, "readwrite");
            IndexedDBObjectStore theObjectStore = theTransction.objectStore(FILE_DATASTORE);
            IndexedDBFile theFile = IndexedDBFile.createCached(aFileName, aBlob);
            IndexedDBRequest theRequest = theObjectStore.put(theFile, IndexedDBFile.createFileKey(aFileName));
            theRequest.setOnerror((aErrorEvent) -> aRejector.reject("Error storing file " + aFileName, null));
            theRequest.setOnsuccess((aSuccessEvent) -> aResolver.resolve(theFile));
        });
    }

    @Override
    public Promise<File, String> updateFile(String aFileName, Blob aBlob) {
        return new Promise<>((aResolver, aRejector) -> {
            IndexedDBTransaction theTransction = database.transaction(FILE_DATASTORE, "readwrite");
            IndexedDBObjectStore theObjectStore = theTransction.objectStore(FILE_DATASTORE);
            IndexedDBFile theFile = IndexedDBFile.createChanged(aFileName, aBlob);
            IndexedDBRequest theRequest = theObjectStore.put(theFile, IndexedDBFile.createFileKey(aFileName));
            theRequest.setOnerror((aErrorEvent) -> aRejector.reject("Error storing file " + aFileName, null));
            theRequest.setOnsuccess((aSuccessEvent) -> aResolver.resolve(theFile));
        });
    }

    @Override
    public Promise<String, String> asDataURL(File aFile) {
        return new Promise<>((aResolver, aRejector) -> {
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

    @Override
    public Promise<File[], String> listChangedFiles() {
        return new Promise<>((aResolver, aRejector) -> {
            List<File> theResult = new ArrayList<>();
            IndexedDBObjectStore theObjectStore = database.transaction(FILE_DATASTORE, "readonly").objectStore(FILE_DATASTORE);
            theObjectStore.openCursor().setOnsuccess(aEvent -> {
                IndexedDBCursor theCursor = aEvent.getTarget().getResult();
                if (theCursor == null) {
                    // Finished with iteration)
                    aResolver.resolve(theResult.toArray(new File[theResult.size()]));
                } else {
                    File theFile = theCursor.getValue();
                    if (File.STATUS_CHANGED.equals(theFile.getStatus())) {
                        theResult.add(theFile);
                    }
                    theCursor.continueWithCursor();
                }
            });
        });
    }
}