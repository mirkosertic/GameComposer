package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.ContentAreaFactory;
import de.mirkosertic.gamecomposer.contentarea.ContentAreaFactoryType;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameScene;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ContentAreaFactoryType(clazz = EventSheet.class)
public class EventSheetEditorControllerFactory implements ContentAreaFactory<EventSheet, EventSheetEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public EventSheetEditorController create(EventSheet aEventSheet) {
        try (InputStream fxml = EventSheetEditorControllerFactory.class.getResourceAsStream("EventSheetEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.EventSheetEditor");
            theLoader.setResources(theBundle);
            BorderPane root = theLoader.load(fxml);

            EventSheetEditorController theController = theLoader.getController();

            return theController.initialize(root, aEventSheet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
