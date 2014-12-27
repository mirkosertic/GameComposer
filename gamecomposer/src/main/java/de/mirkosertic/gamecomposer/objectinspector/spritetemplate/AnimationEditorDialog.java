package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.javafx.JavaFXBitmapResource;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.ResourceName;

import insidefx.undecorator.UndecoratorScene;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import java.io.InputStream;

public class AnimationEditorDialog {

    public static Animation performEditingOf(Node aParent, Sprite aSprite, Animation aAnimation, PersistenceManager aPersistenceManager) {
        FXMLLoader theLoader = new FXMLLoader();
        theLoader.setBuilderFactory(new JavaFXBuilderFactory());
        try (InputStream fxml = AnimationEditorDialog.class.getResourceAsStream("AnimationEditorDialog.fxml")) {
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
            theDialog.initializeFor(aPersistenceManager, aSprite.getGameScene(), aAnimation, theModalStage);
            theModalStage.showAndWait();

            return theDialog.getAnimation();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Animation animation;

    @FXML
    private TextField animationName;

    @FXML
    private BorderPane borderPane;

    private Stage stage;

    private ValidationSupport validationSupport;

    private static class ResourceNameGridCell extends GridCell<ResourceName> {

        private final GameResourceLoader resourceLoader;
        private final ImageView imageView;

        public ResourceNameGridCell(GameResourceLoader aResourceLoader) {
            resourceLoader = aResourceLoader;
            imageView = new ImageView();
        }

        @Override
        protected void updateItem(ResourceName aItem, boolean empty) {
            super.updateItem(aItem, empty);

            if (empty) {
                setGraphic(null);
            } else {
                try {
                    JavaFXBitmapResource theResource = (JavaFXBitmapResource) resourceLoader.load(aItem);
                    imageView.setImage(theResource);
                    setGraphic(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initializeFor(PersistenceManager aPersistenceManager, GameScene aGameScene, Animation aAnimation, Stage aStage) {
        animation = aAnimation;
        stage = aStage;

        GridView<ResourceName> theGrid = new GridView<>();
        for (int i=0;i<aAnimation.getSequenceSize();i++) {
            theGrid.getItems().add(aAnimation.getResourceByIndex(i));
        }

        final GameResourceLoader theLoader = aPersistenceManager.createResourceLoaderFor(aGameScene);

        theGrid.setCellFactory(new Callback<GridView<ResourceName>, GridCell<ResourceName>>() {
            @Override
            public GridCell<ResourceName> call(GridView<ResourceName> param) {
                return new ResourceNameGridCell(theLoader);
            }
        });
        theGrid.backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        borderPane.setCenter(theGrid);

        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(animationName, Validator.createEmptyValidator("Name must not be empty"));

        animationName.setText(aAnimation.getName());
    }

    @FXML
    public void onOk() {
        validationSupport.redecorate();
        if (!validationSupport.isInvalid()) {
            stage.close();
        }
    }

    @FXML
    public void onCancel() {
        animation = null;
        stage.close();
    }

    public Animation getAnimation() {
        return animation;
    }
}