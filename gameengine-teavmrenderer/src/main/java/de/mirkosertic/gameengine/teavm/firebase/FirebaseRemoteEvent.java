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
