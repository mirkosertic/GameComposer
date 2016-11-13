/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.GameAssetSelector;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.javafx.JavaFXBitmapResource;
import de.mirkosertic.gameengine.type.Animation;
import de.mirkosertic.gameengine.type.ResourceName;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javax.inject.Inject;

public class AnimationEditorDialog {

    private static final ResourceName ADD_RESOURCE_MARKER = new ResourceName("ADDRESOURCEMARKER");

    private Animation animation;

    @Inject
    GameAssetSelector gameAssetSelector;

    @Inject
    PersistenceManager persistenceManager;

    @FXML
    private TextField animationName;

    @FXML
    private BorderPane borderPane;

    private Stage stage;

    private ValidationSupport validationSupport;

    private GridView<ResourceName> gridView;

    private GameScene gameScene;

    private class ResourceNameGridCell extends GridCell<ResourceName> {

        private final GameResourceLoader resourceLoader;
        private final HiddenSidesPane singleResourceNode;
        private final ImageView imageView;
        private final Button addAnimationButton;

        public ResourceNameGridCell(GameResourceLoader aResourceLoader) {
            resourceLoader = aResourceLoader;
            singleResourceNode = new HiddenSidesPane();
            imageView = new ImageView();
            singleResourceNode.setContent(imageView);

            Button theDeleteButton = new Button("Remove");
            theDeleteButton.setOnAction((e) -> removeImageFromAnimationSequence(getIndex()));
            singleResourceNode.setBottom(theDeleteButton);
            singleResourceNode.setAnimationDelay(Duration.millis(50));

            addAnimationButton = new Button("Add...");
            addAnimationButton.setOnAction((e) -> addImageToAnimationSequence());
        }

        @Override
        protected void updateItem(ResourceName aItem, boolean empty) {
            super.updateItem(aItem, empty);

            if (empty) {
                setGraphic(null);
            } else {
                if (aItem == ADD_RESOURCE_MARKER) {
                    setGraphic(addAnimationButton);
                } else {
                    resourceLoader.load(aItem).thenContinue(aResult -> {
                        imageView.setImage((JavaFXBitmapResource) aResult);
                    });
                }
            }
        }
    }

    public void initialize(GameScene aGameScene, Animation aAnimation, Stage aStage) {
        stage = aStage;
        gameScene = aGameScene;

        gridView = new GridView<>();
        final GameResourceLoader theLoader = persistenceManager.createResourceLoaderFor(aGameScene);

        gridView.setCellFactory(param -> new ResourceNameGridCell(theLoader));
        gridView.backgroundProperty().set(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        updateAnimationSequence(aAnimation);

        borderPane.setCenter(gridView);

        validationSupport = new ValidationSupport();
        validationSupport.registerValidator(animationName, Validator.createEmptyValidator("Name must not be empty"));

        animationName.setText(aAnimation.getName());
    }

    private void updateAnimationSequence(Animation aAnimation) {

        gridView.getItems().clear();
        for (int i=0;i<aAnimation.getSequenceSize();i++) {
            gridView.getItems().add(aAnimation.getResourceByIndex(i));
        }
        gridView.getItems().add(ADD_RESOURCE_MARKER);

        animation = aAnimation;
    }

    private void addImageToAnimationSequence() {
        ResourceName theNewImage = gameAssetSelector.selectImageAssetFrom(gameScene, borderPane.getScene().getWindow());
        if (theNewImage != null) {
            updateAnimationSequence(animation.addToAnimationSequence(theNewImage));
        }
    }

    private void removeImageFromAnimationSequence(int aIndex) {
        updateAnimationSequence(animation.removeFromAnimationSequence(aIndex));
    }

    @FXML
    public void onOk() {
        validationSupport.redecorate();
        if (!validationSupport.isInvalid()) {
            animation = animation.changeName(animationName.getText());
            stage.close();
        }
    }

    @FXML
    public void onCancel() {
        animation = null;
        stage.close();
    }

    public Animation performEditing() {
        stage.showAndWait();
        return animation;
    }
}