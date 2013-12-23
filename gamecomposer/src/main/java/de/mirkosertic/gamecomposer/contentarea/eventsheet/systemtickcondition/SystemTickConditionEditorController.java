package de.mirkosertic.gamecomposer.contentarea.eventsheet.systemtickcondition;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gameengine.core.SystemTickCondition;

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

    public Node getView() {
        return view;
    }
}