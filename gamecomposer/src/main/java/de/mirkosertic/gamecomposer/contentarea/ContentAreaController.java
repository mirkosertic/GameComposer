package de.mirkosertic.gamecomposer.contentarea;

import de.mirkosertic.gamecomposer.ApplicationStartedEvent;
import de.mirkosertic.gamecomposer.ChildController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import javax.enterprise.event.Observes;
import javax.inject.Singleton;

@Singleton
public class ContentAreaController implements ChildController {

    @FXML
    TabPane editorTabPane;

    @FXML
    BorderPane welcomeBorderPane;

    private Node view;

    ContentAreaController initialize(Node aView) {
        view = aView;
        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    public void onApplicationStarted(@Observes ApplicationStartedEvent aEvent) {
        editorTabPane.getTabs().clear();
        editorTabPane.setVisible(false);
        welcomeBorderPane.setVisible(true);
    }
}
