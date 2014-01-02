package de.mirkosertic.gamecomposer.objectinspector.spritetemplate;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactoryType;
import de.mirkosertic.gameengine.sprite.Sprite;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ObjectInspectorFactoryType(clazz = Sprite.class)
public class SpriteTemplateEditorControllerFactory implements ObjectInspectorFactory<Sprite, SpriteTemplateEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public SpriteTemplateEditorController create(Sprite aObject) {
        try (InputStream fxml = SpriteTemplateEditorController.class.getResourceAsStream("SpriteTemplateEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.spritetemplate.SpriteTemplateEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((SpriteTemplateEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
