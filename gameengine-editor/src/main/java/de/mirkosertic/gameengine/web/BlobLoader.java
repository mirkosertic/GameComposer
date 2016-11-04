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

import org.teavm.jso.ajax.XMLHttpRequest;

public class BlobLoader {

    public interface Callback {
        void loaded(String aURL, Blob response);
    }

    public BlobLoader() {
    }

    public void load(String aURL, Callback aCallback) {

        String theContentType = "application/octet-stream";
        if (aURL.endsWith(".png")) {
            theContentType = "image/png";
        }
        if (aURL.endsWith(".jpg")) {
            theContentType = "image/jpeg";
        }
        if (aURL.endsWith(".wav")) {
            theContentType = "audio/x-wav";
        }
        if (aURL.endsWith(".json")) {
            theContentType = "application/json";
        }

        XMLHttpRequest theRequest = XMLHttpRequest.create();
        theRequest.overrideMimeType(theContentType);
        theRequest.setResponseType("blob");
        theRequest.open("GET", aURL);
        theRequest.onComplete(() -> {
            aCallback.loaded(aURL, (Blob) theRequest.getResponse());
        });
        theRequest.send();
    }
}
