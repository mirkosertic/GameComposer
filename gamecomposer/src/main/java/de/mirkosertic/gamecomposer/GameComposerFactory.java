package de.mirkosertic.gamecomposer;

import insidefx.undecorator.Undecorator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class GameComposerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Inject
    PersistenceManager persistenceManager;

    public void createAndStart(Stage aStage, Application.Parameters aParameters) throws IOException {

        try (InputStream fxml = GameComposerController.class.getResourceAsStream("GameComposer.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.GameComposer");
            theLoader.setResources(theBundle);
            Region root = (Region) theLoader.load(fxml);
            root.getStylesheets().add("/JMetroLightTheme.css");
            ((GameComposerController)theLoader.getController()).initialize(aStage);

            Undecorator theUndecorator = new Undecorator(aStage, root);
            theUndecorator.getStylesheets().add("/skin/undecorator.css");

            Scene theScene = new Scene(theUndecorator);
            theScene.setFill(Color.TRANSPARENT);
            aStage.initStyle(StageStyle.TRANSPARENT);
            aStage.setScene(theScene);
            aStage.show();

            persistenceManager.loadGame(new File("C:\\Users\\Andrea\\IdeaProjects\\GameComposer\\sampleplatformer\\data"));
        }
    }
}