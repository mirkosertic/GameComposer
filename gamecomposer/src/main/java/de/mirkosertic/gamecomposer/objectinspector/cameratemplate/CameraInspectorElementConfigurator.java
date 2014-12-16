package de.mirkosertic.gamecomposer.objectinspector.cameratemplate;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gamecomposer.objectinspector.PropertyEditorItem;
import de.mirkosertic.gamecomposer.objectinspector.utils.CameraTypePropertyEditor;
import de.mirkosertic.gameengine.camera.Camera;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.controlsfx.control.PropertySheet;

@ObjectInspectorElementConfiguratorType(clazz = Camera.class)
public class CameraInspectorElementConfigurator implements ObjectInspectorElementConfigurator<Camera> {

    private static final String CATEGORY_NAME = "Camera";

    @Override
    public List<PropertySheet.Item> getItemsFor(Camera aObject) {
        List<PropertySheet.Item> theResult = new ArrayList<>();
        theResult.add(new PropertyEditorItem<>(CATEGORY_NAME, aObject.typeProperty(), "Type", "The type of the camera", Optional
                .of(CameraTypePropertyEditor.class)));
        return theResult;
    }
}