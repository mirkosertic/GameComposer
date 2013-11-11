package de.mirkosertic.gamecomposer.contentarea;

import de.mirkosertic.gamecomposer.*;
import de.mirkosertic.gameengine.event.PropertyChangeEvent;
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

    void onObjectUpdated(Tab aTab, PropertyChangeEvent aEvent);

    void onFlushResourceCache(FlushResourceCacheEvent aEvent);
}