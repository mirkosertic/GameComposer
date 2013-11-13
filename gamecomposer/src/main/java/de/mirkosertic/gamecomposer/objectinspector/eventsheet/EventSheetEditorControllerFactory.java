package de.mirkosertic.gamecomposer.objectinspector.eventsheet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gameengine.core.EventSheet;

public class EventSheetEditorControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public EventSheetEditorController create(EventSheet aObject) {
        try (InputStream fxml = EventSheetEditorController.class.getResourceAsStream("EventSheetEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.eventsheet.EventSheetEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((EventSheetEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
