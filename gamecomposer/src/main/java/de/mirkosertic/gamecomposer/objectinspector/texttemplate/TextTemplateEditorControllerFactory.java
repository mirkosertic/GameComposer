package de.mirkosertic.gamecomposer.objectinspector.texttemplate;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactoryType;
import de.mirkosertic.gameengine.text.TextComponentTemplate;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@Singleton
@ObjectInspectorFactoryType(clazz = TextComponentTemplate.class)
public class TextTemplateEditorControllerFactory implements ObjectInspectorFactory<TextComponentTemplate, TextTemplateEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public TextTemplateEditorController create(TextComponentTemplate aObject) {
        try (InputStream fxml = TextTemplateEditorController.class.getResourceAsStream("TextTemplateEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.texttemplate.TextTemplateEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((TextTemplateEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
