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

    private static final TextClassInformation CIINSTANCE = new TextClassInformation();

    static final String TYPE = "Text";

    private final GameObjectInstance objectInstance;

    private final Property<Font> font;
    private final Property<Color> color;
    private final Property<TextExpression> textExpression;
    private final Property<Boolean> isScript;

    TextBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getBehaviorTemplate(TextBehaviorTemplate.class));
    }

    TextBehavior(GameObjectInstance aObjectInstance, TextBehaviorTemplate aTemplate) {

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        objectInstance = aObjectInstance;
        font = new Property<>(Font.class, this, FONT_PROPERTY, aTemplate.fontProperty().get(), theEventManager);
        color = new Property<>(Color.class, this, COLOR_PROPERTY, aTemplate.colorProperty().get(), theEventManager);
        textExpression = new Property<>(TextExpression.class, this, TEXT_EXPRESSION_PROPERTY, aTemplate.textExpressionProperty().get(), theEventManager);
        isScript = new Property<>(Boolean.class, this, ISSCRIPT_PROPERTY, aTemplate.isScriptProperty().get(), theEventManager);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public TextClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public Property<Font> fontProperty() {
        return font;
    }

    @Override
    public Property<Color> colorProperty() {
        return color;
    }

    @Override
    public Property<TextExpression> textExpressionProperty() {
        return textExpression;
    }

    @Override
    public Property<Boolean> isScriptProperty() {
        return isScript;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        theResult.put(FONT_PROPERTY, font.get().serialize());
        theResult.put(TEXT_EXPRESSION_PROPERTY, textExpression.get().serialize());
        theResult.put(COLOR_PROPERTY, color.get().serialize());
        theResult.put(ISSCRIPT_PROPERTY, Boolean.toString(isScript.get()));
        return theResult;
    }

    @Override
    public TextBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getBehaviorTemplate(TextBehaviorTemplate.class);
    }

    @Override
    public void delete() {
        objectInstance.getOwnerGameObject().getGameScene().removeBehaviorFrom(objectInstance.getOwnerGameObject(), this);
    }

    public static TextBehavior deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        TextBehavior theTemplate =  new TextBehavior(aObjectInstance);
        theTemplate.font.setQuietly(Font.deserialize((Map<String, Object>) aSerializedData.get(FONT_PROPERTY)));
        theTemplate.textExpression.setQuietly(TextExpression.deserialize((Map<String, Object>) aSerializedData.get(TEXT_EXPRESSION_PROPERTY)));
        theTemplate.color.setQuietly(Color.deserialize((Map<String, Object>) aSerializedData.get(COLOR_PROPERTY)));

        String theIsScript = (String) aSerializedData.get(ISSCRIPT_PROPERTY);
        if (theIsScript != null) {
            theTemplate.isScript.setQuietly(Boolean.valueOf(theIsScript));
        }

        return theTemplate;
    }
}