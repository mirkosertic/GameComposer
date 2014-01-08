package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.TextExpression;

public interface Text {

    String FONT_PROPERTY = "font";
    String COLOR_PROPERTY = "color";
    String TEXT_EXPRESSION_PROPERTY = "textExpression";
    String[] EDITABLE_PROPERTIES = {FONT_PROPERTY, COLOR_PROPERTY, TEXT_EXPRESSION_PROPERTY};

    Property<Font> fontProperty();

    Property<TextExpression> textExpressionProperty();

    Property<Color> colorProperty();
}
