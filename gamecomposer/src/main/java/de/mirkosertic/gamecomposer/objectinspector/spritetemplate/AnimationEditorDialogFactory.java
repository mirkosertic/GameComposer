package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.type.Animation;
import insidefx.undecorator.UndecoratorScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.inject.Inject;
import java.io.InputStream;

public class AnimationEditorDialogFactory {

    @Inject
    FXMLLoaderFactory loaderFactory;

    public AnimationEditorDialog createFor(Node aParent, Sprite aSprite, Animation aAnimation) {
        try (InputStream fxml = AnimationEditorDialog.class.getResourceAsStream("AnimationEditorDialog.fxml")) {
            FXMLLoader theLoader = loaderFactory.createLoader();
            BorderPane thePane = theLoader.load(fxml);

            AnimationEditorDialog theDialog = theLoader.getController();

            Stage theModalStage = new Stage();
            theModalStage.setResizable(false);
            theModalStage.setTitle("Edit animation");
            UndecoratorScene theUndecoratorScene = new UndecoratorScene(theModalStage, thePane);

            // Hacky, but works
            theUndecoratorScene.getUndecorator().setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

            theModalStage.initStyle(StageStyle.TRANSPARENT);
            theUndecoratorScene.setFill(Color.TRANSPARENT);

            theModalStage.setScene(theUndecoratorScene);
            theModalStage.initModality(Modality.APPLICATION_MODAL);
            theModalStage.initOwner(aParent.getScene().getWindow());
            theDialog.initialize(aSprite.getGameScene(), aAnimation, theModalStage);

            return theDialog;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
