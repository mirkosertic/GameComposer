package de.mirkosertic.gamecomposer.objectinspector;

import org.controlsfx.control.PropertySheet;

import java.util.List;

public interface ObjectInspectorElementConfigurator<T> {

    List<PropertySheet.Item> getItemsFor(T aObject);
}
