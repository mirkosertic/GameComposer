package de.mirkosertic.gamecomposer.contentarea.eventsheet.runscript;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionEditorType;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.setproperty.SetPropertyEditorController;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.SetPropertyAction;
import de.mirkosertic.gameengine.script.RunScriptAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

@ActionEditorType(clazz = RunScriptAction.class)
public class RunScriptControllerFactory implements ActionControllerFactory<RunScriptController, RunScriptAction> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public RunScriptController createFor(GameScene aGameScene, GameRule aRule, RunScriptAction anAction) {
        try (InputStream fxml = RunScriptController.class.getResourceAsStream("RunScriptEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.runscript.RunScriptEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = theLoader.load(fxml);

            RunScriptController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aRule, anAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}