package de.mirkosertic.gamecomposer.contentarea.eventsheet.killprocessesforinstance;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionEditorType;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.process.KillProcessesForInstanceAction;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ActionEditorType(clazz = KillProcessesForInstanceAction.class)
public class KillProcessesForInstanceEditorControllerFactory implements ActionControllerFactory<KillProcessesForInstanceEditorController, KillProcessesForInstanceAction> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public KillProcessesForInstanceEditorController createFor(GameScene aGameScene, KillProcessesForInstanceAction aAction) {
        try (InputStream fxml = KillProcessesForInstanceEditorController.class.getResourceAsStream("KillInstanceProcessEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.killinstanceprocesses.KillInstanceProcessEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = (BorderPane) theLoader.load(fxml);

            KillProcessesForInstanceEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
