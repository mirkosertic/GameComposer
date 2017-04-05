package de.mirkosertic.gameengine.teavmwasm;

import org.teavm.interop.Export;

import java.util.ArrayList;
import java.util.List;

public class WASMStringPool {

    private final static List<StringBuffer> BUFFERS = new ArrayList<>();

    @Export(name = "newStringPoolID")
    public static int newStringPoolIO() {
        BUFFERS.add(new StringBuffer());
        return BUFFERS.size() - 1;
    }

    @Export(name = "addCharToStringPool")
    public static void addCharToStringPool(int aPoolID, int aChar) {
        BUFFERS.get(aPoolID).append((char) aChar);
    }
}
