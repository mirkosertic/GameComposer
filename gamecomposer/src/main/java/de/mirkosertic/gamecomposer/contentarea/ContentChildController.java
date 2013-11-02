package de.mirkosertic.gamecomposer.contentarea;

import de.mirkosertic.gamecomposer.ChildController;
import javafx.scene.input.KeyEvent;

public interface ContentChildController<T> extends ChildController {

    T getEditingObject();

    void processKeyPressedEvent(KeyEvent aKeyEvent);

    void processKeyReleasedEvent(KeyEvent aKeyEvent);

    void addedAsTab();

    void removed();
}
