package de.mirkosertic.gamecomposer;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class GameComposerController {

    @FXML
    Accordion propertiesAccordion;

    @FXML
    TreeView projectStructureTreeView;

    @FXML
    TabPane editorTabPane;

    @FXML
    BorderPane welcomeBorderPane;

    public void initialize(Stage aStage) {
        editorTabPane.getTabs().clear();
        welcomeBorderPane.setVisible(true);
        editorTabPane.setVisible(false);
    }

    @FXML
    public void onClose() {
        Platform.exit();
    }
}
