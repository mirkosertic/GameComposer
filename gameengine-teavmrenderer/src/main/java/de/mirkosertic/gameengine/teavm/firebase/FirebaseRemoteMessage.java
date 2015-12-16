package de.mirkosertic.gameengine.teavm.firebase;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSIndexer;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public abstract class FirebaseRemoteMessage implements JSObject {

    @JSBody(
            params = {},
            script = "return new Object();"
    )
    public static native FirebaseRemoteMessage create();

    @JSIndexer
    public abstract void putString(String aKey, String aValue);

    @JSIndexer
    public abstract void putObject(String aKey, FirebaseRemoteMessage aValue);

    @JSIndexer
    public abstract String getString(String aKey);

    @JSIndexer
    public abstract FirebaseRemoteMessage getObject(String aKey);

    @JSProperty(value = "keys")
    public abstract void setKeys(String[] aKeys);

    @JSProperty(value = "keys")
    public abstract String[] getKeys();
}
