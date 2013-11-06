package de.mirkosertic.gamecomposer.contentarea;

import de.mirkosertic.gamecomposer.*;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;

public interface ContentChildController<T> extends ChildController {

    T getEditingObject();

    void processKeyPressedEvent(KeyEvent aKeyEvent);

    void processKeyReleasedEvent(KeyEvent aKeyEvent);

    void addedAsTab();

    void removed();

    void onObjectSelected(ObjectSelectedEvent aEvent);

    void onShutdown(ShutdownEvent aEvent);

    void onObjectUpdated(Tab aTab, ObjectUpdatedEvent aEvent);

    void onFlushResourceCache(FlushResourceCacheEvent aEvent);
}
