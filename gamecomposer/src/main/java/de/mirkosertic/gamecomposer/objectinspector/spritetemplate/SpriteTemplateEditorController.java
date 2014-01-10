package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.FlushResourceCacheEvent;
import de.mirkosertic.gamecomposer.GameAssetSelector;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.javafx.JavaFXBitmapResource;

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

public class SpriteTemplateEditorController implements ObjectInspectorElementController {

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
    private Sprite object;

    @Override
    public void cleanup() {
    }

    GameObject getGameObjectOwner() {
        if (object instanceof SpriteBehavior) {
            return ((SpriteBehavior) object).getTemplate().getOwner();
        }
        if (object instanceof SpriteBehaviorTemplate) {
            return ((SpriteBehaviorTemplate) object).getOwner();
        }
        throw new IllegalArgumentException("Unknown type : "+object);
    }

    public SpriteTemplateEditorController initialize(Parent aView, Sprite aObject) {
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
            GameResourceLoader theLoader = persistenceManager.createResourceLoaderFor(getGameObjectOwner().getGameScene());
            try {
                JavaFXBitmapResource theResource = (JavaFXBitmapResource) theLoader.load(theResourceName);
                spritePreview.setImage(theResource);

                assetName.setText(theResourceName.name);
                getGameObjectOwner().sizeProperty().set(new Size((int) theResource.getWidth(), (int) theResource.getHeight()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void onSelectSpriteImage() {

        GameScene theScene = getGameObjectOwner().getGameScene();

        ResourceName theNewResourceName = gameAssetSelector.selectImageAssetFrom(theScene, view.getScene().getWindow());
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