package de.mirkosertic.gamecomposer;

import insidefx.undecorator.UndecoratorScene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class GameComposerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    private Application application;

    public void createAndStart(Application aApplication, Stage aStage, Application.Parameters aParameters) throws IOException {

        application = aApplication;

        try (InputStream fxml = GameComposerController.class.getResourceAsStream("GameComposer.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.GameComposer");
            theLoader.setResources(theBundle);
            Region root = theLoader.load(fxml);
            root.getStylesheets().add("/JMetroLightTheme.css");
            ((GameComposerController)theLoader.getController()).initialize(aStage);

            UndecoratorScene theUndecoratorScene = new UndecoratorScene(aStage, root);

            // Hacky, but works
            theUndecoratorScene.getUndecorator().setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

            aStage.initStyle(StageStyle.TRANSPARENT);
            theUndecoratorScene.setFill(Color.TRANSPARENT);

            aStage.setScene(theUndecoratorScene);
            aStage.show();
        }
    }

    @Produces
    @Any
    public Application getApplication() {
        return application;
    }
}