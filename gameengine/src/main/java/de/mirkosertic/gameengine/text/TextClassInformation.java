package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.TextExpression;

public class TextClassInformation extends ClassInformation {

    public static final TextClassInformation INSTANCE = new TextClassInformation();

    public final static Field<Text, Property<Font>> FONT = new Field<Text, Property<Font>>("font", Property.class) {
        @Override
        public Property<Font> getValue(Text aObject) {
            return aObject.fontProperty();
        }
    };

    public final static Field<Text, Property<Color>> COLOR = new Field<Text, Property<Color>>("color", Property.class) {
        @Override
        public Property<Color> getValue(Text aObject) {
            return aObject.colorProperty();
        }
    };

    public final static Field<Text, Property<TextExpression>> TEXTEXPRESSION = new Field<Text, Property<TextExpression>>("textExpression", Property.class) {
        @Override
        public Property<TextExpression> getValue(Text aObject) {
            return aObject.textExpressionProperty();
        }
    };

    private TextClassInformation() {
        register(FONT);
        register(COLOR);
        register(TEXTEXPRESSION);
    }
}
