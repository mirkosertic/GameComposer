package de.mirkosertic.gameengine.web.github;

import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

public interface File extends JSObject {

    String DIR_TYPE = "dir";

    @JSProperty
    String getName();

    @JSProperty
    String getPath();

    @JSProperty
    String getType();

    @JSProperty
    String getUrl();

    @JSProperty
    long getSize();

    @JSProperty
    String getDownload_url();

    @JSProperty
    String getSha();
}
