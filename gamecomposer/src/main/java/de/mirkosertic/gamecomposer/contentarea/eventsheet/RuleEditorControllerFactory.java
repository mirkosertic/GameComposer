package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameRule;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

public class RuleEditorControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public RuleEditorController createFor(EventSheetEditorController aParentController, EventSheet aEventSheet, GameRule aGameRule) {
        try (InputStream fxml = RuleEditorController.class.getResourceAsStream("RuleEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.RuleEditor");
            theLoader.setResources(theBundle);
            BorderPane root = theLoader.load(fxml);

            RuleEditorController theController = theLoader.getController();

            return theController.initialize(aParentController, root, aEventSheet, aGameRule);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
