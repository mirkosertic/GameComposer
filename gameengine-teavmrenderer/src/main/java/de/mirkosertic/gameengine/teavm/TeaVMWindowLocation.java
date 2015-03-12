package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface TeaVMWindowLocation extends JSObject {

    @JSProperty(value = "hash")
    void setHash(String aHash);

    @JSProperty(value = "hash")
    String getHash();

    @JSProperty(value = "search")
    String getSearch();
}
