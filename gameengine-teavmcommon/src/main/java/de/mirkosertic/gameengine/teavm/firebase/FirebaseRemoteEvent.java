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
package de.mirkosertic.gameengine.teavm.firebase;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class FirebaseRemoteEvent implements JSObject {

    @JSProperty("epid")
    public abstract String getEPID();
    @JSProperty("epid")
    public abstract void setEPID(String aID);

    @JSProperty("evts")
    public abstract String getEventTimestamp();
    @JSProperty("evts")
    public abstract void setEventTimestamp(String aTimestamp);

    @JSProperty("payloadSize")
    public abstract int getPayloadSize();
    @JSProperty("payloadSize")
    public abstract void setPayloadSize(int aSize);

    @JSProperty("events")
    public abstract FirebaseRemoteMessage[] getEvents();
    @JSProperty("events")
    public abstract void setEvents(FirebaseRemoteMessage[] aEvents);

    @JSBody(
            params = {},
            script = "return new Object();"
    )
    public native static FirebaseRemoteEvent create();
}
