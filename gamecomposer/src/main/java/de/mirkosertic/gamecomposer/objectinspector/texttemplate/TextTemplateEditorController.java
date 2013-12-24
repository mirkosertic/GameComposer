package de.mirkosertic.gamecomposer.objectinspector.texttemplate;

import de.mirkosertic.gamecomposer.PropertyBinder;
import de.mirkosertic.gamecomposer.StringConverterFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorElementController;
import de.mirkosertic.gameengine.text.Text;
import de.mirkosertic.gameengine.types.Font;
import de.mirkosertic.gameengine.types.TextExpression;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import javax.inject.Inject;

public class TextTemplateEditorController implements ObjectInspectorElementController {

    @Inject
    StringConverterFactory stringConverterFactory;

    @FXML
    ComboBox fontCombobox;

    @FXML
    TextField fontSize;

    @FXML
    ColorPicker textColor;

    @FXML
    TextField textExpression;

    private Parent view;
    private Text textComponentTemplate;

    @Override
    public void cleanup() {
        PropertyBinder.unbind(textComponentTemplate.colorProperty());
        PropertyBinder.unbind(textComponentTemplate.fontProperty());
        PropertyBinder.unbind(textComponentTemplate.colorProperty());
        PropertyBinder.unbind(textComponentTemplate.textExpressionProperty());
    }

    public TextTemplateEditorController initialize(Parent aView, Text aObject) {
        view = aView;
        textComponentTemplate = aObject;

        fontCombobox.getItems().clear();
        fontCombobox.getItems().addAll(Font.FontName.values());
        fontCombobox.setConverter(stringConverterFactory.createFontStringConverter());
        fontCombobox.getSelectionModel().select(aObject.fontProperty().get().name);
        fontCombobox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Font.FontName theNewName = (Font.FontName) fontCombobox.getSelectionModel().getSelectedItem();
                Font theCurrentFont = textComponentTemplate.fontProperty().get();
                textComponentTemplate.fontProperty().set(theCurrentFont.changeName(theNewName));
            }
        });

        PropertyBinder.bind(aObject.colorProperty(), textColor.valueProperty(),
                new PropertyBinder.Converter<de.mirkosertic.gameengine.types.Color, Color>() {
                    @Override
                    public Color beanToUI(de.mirkosertic.gameengine.types.Color aValue) {
                        return Color.rgb(aValue.r, aValue.g, aValue.b);
                    }

                    @Override
                    public de.mirkosertic.gameengine.types.Color uiToBean(Color aValue) {
                        return new de.mirkosertic.gameengine.types.Color((int) (aValue.getRed() * 255), (int) (aValue
                                .getGreen() * 255), (int) (aValue.getBlue() * 255));
                    }
                });
        PropertyBinder.bind(aObject.fontProperty(), fontSize.textProperty(), new PropertyBinder.Converter<Font, String>() {
            @Override
            public String beanToUI(Font aValue) {
                return Integer.toString(aValue.size);
            }

            @Override
            public Font uiToBean(String aValue) {
                int theSize = Integer.parseInt(aValue);
                Font theCurrentFont = textComponentTemplate.fontProperty().get();
                return theCurrentFont.changeSize(theSize);
            }
        });
        PropertyBinder.bind(aObject.textExpressionProperty(), textExpression.textProperty(), new PropertyBinder.Converter<TextExpression, String>() {
            @Override
            public String beanToUI(TextExpression aValue) {
                return aValue.expression;
            }

            @Override
            public TextExpression uiToBean(String aValue) {
                return new TextExpression(aValue);
            }
        });

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}