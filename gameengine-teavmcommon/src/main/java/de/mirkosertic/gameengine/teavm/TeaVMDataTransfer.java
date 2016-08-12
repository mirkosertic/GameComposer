package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSObject;

public interface TeaVMDataTransfer extends JSObject {

    void setData(String aKey, String aValue);

    String getData(String aKey);
}
