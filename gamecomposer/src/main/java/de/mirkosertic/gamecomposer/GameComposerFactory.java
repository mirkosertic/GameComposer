package de.mirkosertic.gamecomposer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class GameComposerFactory {

    @Inject
    FXMLLoader fxmlLoader;

    @Inject
    PersistenceManager persistenceManager;

    public void createAndStart(Stage aStage, Application.Parameters aParameters) throws IOException {

        try (InputStream fxml = GameComposerController.class.getResourceAsStream("GameComposer.fxml")) {
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.GameComposer");
            fxmlLoader.setResources(theBundle);
            Parent root = (Parent) fxmlLoader.load(fxml);
            ((GameComposerController)fxmlLoader.getController()).initialize(aStage);
            aStage.setScene(new Scene(root));
            aStage.show();

            persistenceManager.loadGame(new File("C:\\source\\idea_projects\\GameEngine\\sampleplatformer\\data"));
        }
    }
}