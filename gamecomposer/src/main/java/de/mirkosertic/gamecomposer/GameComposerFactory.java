package de.mirkosertic.gamecomposer;

import insidefx.undecorator.UndecoratorScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class GameComposerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public void createAndStart(Stage aStage, Application.Parameters aParameters) throws IOException {

        try (InputStream fxml = GameComposerController.class.getResourceAsStream("GameComposer.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.GameComposer");
            theLoader.setResources(theBundle);
            Region root = (Region) theLoader.load(fxml);
            root.getStylesheets().add("/JMetroLightTheme.css");
            ((GameComposerController)theLoader.getController()).initialize(aStage);

            UndecoratorScene theUndecoratorScene = new UndecoratorScene(aStage, root);

            aStage.initStyle(StageStyle.TRANSPARENT);
            aStage.setScene(theUndecoratorScene);
            aStage.show();
        }
    }
}