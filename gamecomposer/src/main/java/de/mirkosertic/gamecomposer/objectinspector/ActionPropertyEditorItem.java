package de.mirkosertic.gamecomposer.objectinspector;

import de.mirkosertic.gamecomposer.objectinspector.utils.ActionPropertyEditor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class ActionPropertyEditorItem implements PropertySheet.Item {

    public static class Action {

        private final String name;
        private final EventHandler<ActionEvent> eventHandler;

        public Action(String aName, EventHandler<ActionEvent> aEventHandler) {
            name = aName;
            eventHandler = aEventHandler;
        }

        public String getName() {
            return name;
        }

        public EventHandler<ActionEvent> getEventHandler() {
            return eventHandler;
        }
    }

    private final List<Action> actions;
    private final String category;
    private final String name;
    private final String description;

    public ActionPropertyEditorItem(String aCategory, String aName, String aDescription) {
        actions = new ArrayList<>();
        category = aCategory;
        name = aName;
        description = aDescription;
    }

    @Override
    public Class<?> getType() {
        return Object.class;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {
   }

    public void addAction(Action aAction) {
        actions.add(aAction);
    }

    public Stream<Action> actionStream() {
        return actions.stream();
    }

    @Override
    public Optional<Class<? extends PropertyEditor<?>>> getPropertyEditorClass() {
        return Optional.of(ActionPropertyEditor.class);
    }
}