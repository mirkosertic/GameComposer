package de.mirkosertic.gamecomposer.objectinspector.cameratemplate;

import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfigurator;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementConfiguratorType;
import de.mirkosertic.gameengine.camera.Camera;
import org.controlsfx.control.PropertySheet;

import java.util.ArrayList;
import java.util.List;

@ObjectInspectorElementConfiguratorType(clazz = Camera.class)
public class CameraInspectorElementConfigurator implements ObjectInspectorElementConfigurator<Camera> {

    @Override
    public List<PropertySheet.Item> getItemsFor(Camera aObject) {
        return new ArrayList<>();
    }
}