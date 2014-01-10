package de.mirkosertic.gameengine.text;

import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Font;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.TextExpression;

import java.util.HashMap;
import java.util.Map;

public class TextBehavior implements Behavior, Text, Reflectable<TextClassInformation> {

    static final String TYPE = "Text";

    private final GameObjectInstance objectInstance;

    private final Property<Font> font;
    private final Property<Color> color;
    private final Property<TextExpression> textExpression;

    TextBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getComponentTemplate(TextBehaviorTemplate.class));
    }

    TextBehavior(GameObjectInstance aObjectInstance, TextBehaviorTemplate aTemplate) {

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        objectInstance = aObjectInstance;
        font = new Property<Font>(Font.class, this, FONT_PROPERTY, aTemplate.fontProperty().get(), theEventManager);
        color = new Property<Color>(Color.class, this, COLOR_PROPERTY, aTemplate.colorProperty().get(), theEventManager);
        textExpression = new Property<TextExpression>(TextExpression.class, this, TEXT_EXPRESSION_PROPERTY, aTemplate.textExpressionProperty().get(), theEventManager);
    }

    @Override
    public TextClassInformation getClassInformation() {
        return TextClassInformation.INSTANCE;
    }

    public Property<Font> fontProperty() {
        return font;
    }

    public Property<Color> colorProperty() {
        return color;
    }

    public Property<TextExpression> textExpressionProperty() {
        return textExpression;
    }

    @Override
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        theResult.put(FONT_PROPERTY, font.get().serialize());
        theResult.put(TEXT_EXPRESSION_PROPERTY, textExpression.get().serialize());
        theResult.put(COLOR_PROPERTY, color.get().serialize());
        return theResult;
    }

    @Override
    public TextBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(TextBehaviorTemplate.class);
    }

    public static TextBehavior deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        TextBehavior theTemplate =  new TextBehavior(aObjectInstance);
        theTemplate.font.setQuietly(Font.deserialize((Map<String, Object>) aSerializedData.get(FONT_PROPERTY)));
        theTemplate.textExpression.setQuietly(TextExpression.deserialize((Map<String, Object>) aSerializedData.get(TEXT_EXPRESSION_PROPERTY)));
        theTemplate.color.setQuietly(Color.deserialize((Map<String, Object>) aSerializedData.get(COLOR_PROPERTY)));
        return theTemplate;
    }
}