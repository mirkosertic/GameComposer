package de.mirkosertic.gamecomposer.contentarea;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.ShutdownEvent;
import javafx.scene.input.KeyEvent;

public interface ContentChildController<T> extends ChildController {

    T getEditingObject();

    void processKeyPressedEvent(KeyEvent aKeyEvent);

    void processKeyReleasedEvent(KeyEvent aKeyEvent);

    void addedAsTab();

    void removed();

    void onObjectSelected(ObjectSelectedEvent aEvent);

    void onShutdown(ShutdownEvent aEvent);
}
