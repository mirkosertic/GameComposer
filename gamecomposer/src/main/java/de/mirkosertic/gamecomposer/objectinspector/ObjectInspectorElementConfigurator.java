package de.mirkosertic.gamecomposer.objectinspector;

import java.util.List;
import org.controlsfx.control.PropertySheet;

public interface ObjectInspectorElementConfigurator<T> {

    List<PropertySheet.Item> getItemsFor(T aObject);
}
