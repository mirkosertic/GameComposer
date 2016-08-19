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
package de.mirkosertic.gamecomposer.contentarea.eventsheet.systemtickcondition;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gameengine.action.SystemTickCondition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class SystemTickConditionEditorController implements Controller {

    @Inject
    StringConverterFactory stringConverterFactory;

    @FXML
    TextField everyTicksTextField;

    private SystemTickCondition condition;
    private Node view;

    SystemTickConditionEditorController initialize(Node aView, SystemTickCondition aCondition) {
        view = aView;
        condition = aCondition;

        PropertyBinder.bindUIToBean(condition.everyTicksProperty(), everyTicksTextField.textProperty(), new PropertyBinder.Converter<Long, String>() {
            @Override
            public String beanToUI(Long aValue) {
                return aValue.toString();
            }

            @Override
            public Long uiToBean(String aValue) {
                return Long.valueOf(aValue);
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}