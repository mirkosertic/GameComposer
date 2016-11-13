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
package de.mirkosertic.gamecomposer.objectinspector.utils;

import de.mirkosertic.gamecomposer.objectinspector.PersistentPropertyEditorItem;
import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.javafx.JavaFXBitmapResource;
import de.mirkosertic.gameengine.sprite.Sprite;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.type.ResourceName;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

import java.io.File;

public class ResourceNameImagePropertyEditor implements PropertyEditor<ResourceName> {

    private ResourceName value;

    private final VBox editor;
    private final ImageView imageView;
    private final Hyperlink selectResource;

    private PersistentPropertyEditorItem<ResourceName> item;

    public ResourceNameImagePropertyEditor(PropertySheet.Item aItem) {

        item = (PersistentPropertyEditorItem<ResourceName>) aItem;

        imageView = new ImageView();

        selectResource = new Hyperlink("Select resource");
        selectResource.setOnAction(event -> selectResource());

        editor = new VBox();
        editor.getChildren().addAll(imageView, selectResource);
    }

    @Override
    public Node getEditor() {
        return editor;
    }

    @Override
    public ResourceName getValue() {
        return value;
    }

    private void selectResource() {
        FileChooser theChooser = new FileChooser();
        theChooser.setTitle("Select resource");
        GameScene theGameScene = getGameScene();
        theChooser.setInitialDirectory(item.getPersistenceManager().getAssetsDirectoryFor(theGameScene));
        theChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Images","png"));
        File theNewFile = theChooser.showOpenDialog(editor.getScene().getWindow());
        if (theNewFile != null) {
            ResourceName theNewName = item.getPersistenceManager().toResourceName(theGameScene, theNewFile);
            item.setValue(theNewName);
            setValue(theNewName);
        }
    }

    private GameScene getGameScene() {
        Sprite theSprite = (Sprite) item.getOwner();
        if (theSprite instanceof Behavior) {
            SpriteBehavior theSpriteBehavior = (SpriteBehavior) theSprite;
            return theSpriteBehavior.getTemplate().getOwner().getGameScene();
        }
        if (theSprite instanceof BehaviorTemplate) {
            SpriteBehaviorTemplate theSpriteBehaviorTemplate = (SpriteBehaviorTemplate) theSprite;
            return theSpriteBehaviorTemplate.getOwner().getGameScene();
        }
        throw new IllegalStateException("No sprite instance!");
    }

    @Override
    public void setValue(ResourceName aValue) {
        value = aValue;
        GameScene theGameScene = getGameScene();
        GameResourceLoader theLoader = item.getPersistenceManager().createResourceLoaderFor(theGameScene);
        theLoader.load(aValue).thenContinue(aResult -> {
            imageView.setImage((JavaFXBitmapResource) aResult);
        });
    }
}