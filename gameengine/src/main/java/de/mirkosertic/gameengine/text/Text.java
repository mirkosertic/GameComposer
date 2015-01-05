package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.TextExpression;

public interface Text {

    String FONT_PROPERTY = "font";
    String COLOR_PROPERTY = "color";
    String TEXT_EXPRESSION_PROPERTY = "textExpression";

    @ReflectiveField
    Property<Font> fontProperty();

    @ReflectiveField
    Property<TextExpression> textExpressionProperty();

    @ReflectiveField
    Property<Color> colorProperty();

    @ReflectiveMethod
    void delete();
}