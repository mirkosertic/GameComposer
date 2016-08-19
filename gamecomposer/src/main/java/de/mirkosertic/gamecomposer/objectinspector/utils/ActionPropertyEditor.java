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

import de.mirkosertic.gamecomposer.objectinspector.ActionPropertyEditorItem;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;
import org.controlsfx.control.PropertySheet;
import org.controlsfx.property.editor.PropertyEditor;

public class ActionPropertyEditor implements PropertyEditor<Object> {

    private final VBox editor;
    private ActionPropertyEditorItem item;

    public ActionPropertyEditor(PropertySheet.Item aItem) {

        editor = new VBox();
        editor.setAlignment(Pos.BASELINE_LEFT);

        item = (ActionPropertyEditorItem) aItem;
        item.actionStream().forEach(new Consumer<ActionPropertyEditorItem.Action>() {
            @Override
            public void accept(ActionPropertyEditorItem.Action aAction) {
                Hyperlink theHyperlink = new Hyperlink(aAction.getName());
                theHyperlink.setOnAction(aAction.getEventHandler());
                editor.getChildren().add(theHyperlink);
            }
        });
    }

    @Override
    public Node getEditor() {
        return editor;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object aValue) {
    }
}