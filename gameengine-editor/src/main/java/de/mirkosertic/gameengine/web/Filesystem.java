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
package de.mirkosertic.gameengine.web;

public interface Filesystem {

    interface FileProcessor {

        void process(File aFile);

        void doesNotExist(String aFileName);
    }

    interface DataUrlCallback {

        void handle(String aValue);
    }

    void openFile(String aFileName, FileProcessor aProcessor);

    void storeFile(String aFileName, Blob aBlob, FileProcessor aProcessor);

    void updateFile(String aFileName, Blob aBlob, FileProcessor aProcessor);

    void asDataURL(File aFile, DataUrlCallback aCallback);
}