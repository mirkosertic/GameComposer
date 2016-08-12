package de.mirkosertic.gameengine.teavm;

import org.teavm.jso.JSProperty;
import org.teavm.jso.dom.events.MouseEvent;

public interface TeaVMDragEvent extends MouseEvent {

    @JSProperty
    TeaVMDataTransfer getDataTransfer();
}
