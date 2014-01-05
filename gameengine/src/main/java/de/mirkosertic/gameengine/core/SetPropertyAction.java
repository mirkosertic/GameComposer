package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;
import de.mirkosertic.gameengine.type.DefaultValueProvider;
import de.mirkosertic.gameengine.type.TextExpression;

import java.util.HashMap;
import java.util.Map;

public class SetPropertyAction implements Action {

    public static final String TYPE_VALUE = "SetPropertyAction";
    public static final String PROPERTY_NAME_PROPERTY = "propertyName";
    public static final String PROPERTY_VALUE_PROPERTY = "value";

    private final Property<String> propertyName;
    private final Property<TextExpression> propertyValue;

    @UsedByReflection
    public SetPropertyAction() {
        propertyName = new Property<String>(this, PROPERTY_NAME_PROPERTY, (String) null);
        propertyValue = new Property<TextExpression>(this, PROPERTY_VALUE_PROPERTY, (TextExpression) null);
    }

    public Property<String> propertyNameProperty() {
        return propertyName;
    }

    public Property<TextExpression> propertyValueProperty() {
        return propertyValue;
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aConditionResult) {
        if (!propertyValue.isNull()) {

            String thePropertyName = propertyName.get();

            GameEvent theEvent = aConditionResult.getEvent();

            ExpressionParser theParser = aScene.get(propertyValue.get());

            for (GameObjectInstance theInstance : aConditionResult.getAffectedInstances()) {

                theParser.registerVariable(ExpressionParser.INSTANCE_VARIABLE, theInstance);

                Map<String, Object> theValues = theEvent.getMemberValues();
                for (Map.Entry<String, Object> theEntry : theValues.entrySet()) {
                    theParser.registerVariable(ExpressionParser.EVENT_PREFIX + theEntry.getKey(), new DefaultValueProvider<Object>(theEntry.getValue()));
                }

                ReadOnlyProperty theProperty = theInstance.getPropertyByName(thePropertyName);
                theParser.registerVariable(ExpressionParser.OLD_VALUE_VARIABLE, theProperty);

                Object theNewPropertyValue = theParser.evaluateToObject();

                theInstance.setPropertyByName(thePropertyName, theNewPropertyValue);
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put(PROPERTY_NAME_PROPERTY, propertyName.get());
        theResult.put(PROPERTY_VALUE_PROPERTY, propertyValue.get());
        return theResult;
    }

    public static SetPropertyAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        SetPropertyAction theResult = new SetPropertyAction();
        theResult.propertyName.setQuietly((String) aSerializedData.get(PROPERTY_NAME_PROPERTY));
        String theValue = (String) aSerializedData.get(PROPERTY_VALUE_PROPERTY);
        if (theValue != null) {
            theResult.propertyName.setQuietly(theValue);
        }
        return theResult;
    }
}