package de.mirkosertic.gamecomposer.objectinspector.statictemplate;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactoryType;
import de.mirkosertic.gameengine.physic.Static;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ObjectInspectorFactoryType(clazz = Static.class)
public class StaticTemplateEditorControllerFactory implements ObjectInspectorFactory<Static, StaticTemplateEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public StaticTemplateEditorController create(Static aObject) {
        try (InputStream fxml = StaticTemplateEditorController.class.getResourceAsStream("StaticTemplateEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.statictemplate.StaticTemplateEditor");
            theLoader.setResources(theBundle);
            Parent root = theLoader.load(fxml);
            return ((StaticTemplateEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
