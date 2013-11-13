package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.FlushResourceCacheEvent;
import de.mirkosertic.gamecomposer.GameAssetSelector;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorChildController;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.types.ResourceName;
import de.mirkosertic.gameengine.types.Size;
import de.mirkosertic.gameengine.javafx.JavaFXBitmapResource;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.IOException;

public class SpriteTemplateEditorController implements ObjectInspectorChildController {

    @FXML
    ImageView spritePreview;

    @FXML
    Hyperlink selectSpriteImage;

    @FXML
    TextField assetName;

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    Event<FlushResourceCacheEvent> flushResourceCacheEvent;

    @Inject
    GameAssetSelector gameAssetSelector;

    private Parent view;
    private SpriteComponentTemplate object;

    @Override
    public void cleanup() {
    }

    public SpriteTemplateEditorController initialize(Parent aView, SpriteComponentTemplate aObject) {
        view = aView;
        object = aObject;

        reloadSprite();
        selectSpriteImage.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                onSelectSpriteImage();
            }
        });

        return this;
    }

    private void reloadSprite() {
        ResourceName theResourceName = object.resourceNameProperty().get();
        assetName.setText("");
        if (theResourceName != null && theResourceName.name != null) {
            GameResourceLoader theLoader = persistenceManager.createResourceLoaderFor(object.getOwner().getGameScene());
            try {
                JavaFXBitmapResource theResource = (JavaFXBitmapResource) theLoader.load(theResourceName);
                spritePreview.setImage(theResource);

                assetName.setText(theResourceName.name);
                object.getOwner().sizeProperty().set(new Size((int) theResource.getWidth(), (int) theResource.getHeight()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void onSelectSpriteImage() {

        GameScene theScene = object.getOwner().getGameScene();

        ResourceName theNewResourceName = gameAssetSelector.selectImageAssetFrom(theScene);
        if (theNewResourceName != null) {
            object.resourceNameProperty().set(theNewResourceName);

            reloadSprite();

            flushResourceCacheEvent.fire(new FlushResourceCacheEvent());
        }
    }

    @Override
    public Node getView() {
        return view;
    }
}