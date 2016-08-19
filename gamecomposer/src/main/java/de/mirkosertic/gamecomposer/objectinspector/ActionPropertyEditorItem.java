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
package de.mirkosertic.gamecomposer.objectinspector;

import de.mirkosertic.gamecomposer.objectinspector.utils.ActionPropertyEditor;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

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