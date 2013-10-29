package de.mirkosertic.gamecomposer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import javax.enterprise.event.Event;
import javax.inject.Inject;

public class GameComposerController {

    @Inject
    @ObjectInspector
    ChildController objectInspector;

    @Inject
    @ContentArea
    ChildController contentArea;

    @Inject
    @ProjectStructure
    ChildController projectStructure;

    @Inject
    Event<ApplicationStartedEvent> applicationStartedEvent;

    @FXML
    HBox childViews;

    public void initialize(Stage aStage) {
        childViews.getChildren().add(objectInspector.getView());
        childViews.getChildren().add(contentArea.getView());
        childViews.getChildren().add(projectStructure.getView());

        HBox.setHgrow(objectInspector.getView(), Priority.SOMETIMES);
        HBox.setHgrow(contentArea.getView(), Priority.ALWAYS);
        HBox.setHgrow(projectStructure.getView(), Priority.SOMETIMES);

        applicationStartedEvent.fire(new ApplicationStartedEvent());
    }

    @FXML
    public void onClose() {
        Platform.exit();
    }
}
