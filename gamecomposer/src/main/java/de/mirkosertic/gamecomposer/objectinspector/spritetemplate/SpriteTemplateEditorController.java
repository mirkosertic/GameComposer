package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.ChildController;
import de.mirkosertic.gamecomposer.FlushResourceCacheEvent;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.ResourceName;
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
import javafx.stage.FileChooser;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

public class SpriteTemplateEditorController implements ChildController {

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

    private Parent view;
    private SpriteComponentTemplate object;

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
        if (theResourceName != null && theResourceName.getName() != null) {
            GameResourceLoader theLoader = persistenceManager.createResourceLoaderFor(object.getOwner().getGameScene());
            try {
                JavaFXBitmapResource theResource = (JavaFXBitmapResource) theLoader.load(theResourceName);
                spritePreview.setImage(theResource);

                assetName.setText(theResourceName.getName());
                object.getOwner().sizeProperty().set(new Size((int) theResource.getWidth(), (int) theResource.getHeight()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void onSelectSpriteImage() {

        GameScene theScene = object.getOwner().getGameScene();

        FileChooser theFileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)","*.png");
        theFileChooser.getExtensionFilters().add(extFilter);
        theFileChooser.setInitialDirectory(persistenceManager.getAssetsDirectoryFor(theScene));

        File theSelectedFile = theFileChooser.showOpenDialog(null);
        if (theSelectedFile != null) {
            ResourceName theName = persistenceManager.toResourceName(theScene, theSelectedFile);
            object.resourceNameProperty().set(theName);

            reloadSprite();

            flushResourceCacheEvent.fire(new FlushResourceCacheEvent());
        }
    }

    @Override
    public Node getView() {
        return view;
    }
}