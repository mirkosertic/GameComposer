package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.expression.PropertyDiscoverer;
import de.mirkosertic.gameengine.type.TextExpression;

import java.util.HashMap;
import java.util.Map;

public class SetPropertyAction implements Action {

    public static final String TYPE_VALUE = "SetPropertyAction";
    public static final String PROPERTY_GAMEOBJECT_PROPERTY = "gameObject";
    public static final String PROPERTY_NAME_PROPERTY = "propertyName";
    public static final String PROPERTY_VALUE_PROPERTY = "value";

    private final Property<GameObject> gameObject;
    private final Property<String> propertyName;
    private final Property<TextExpression> propertyValue;

    @UsedByReflection
    public SetPropertyAction() {
        propertyName = new Property<>(String.class, this, PROPERTY_NAME_PROPERTY, (String) null);
        gameObject = new Property<>(GameObject.class, this, PROPERTY_GAMEOBJECT_PROPERTY, (GameObject) null);
        propertyValue = new Property<>(TextExpression.class, this, PROPERTY_VALUE_PROPERTY, (TextExpression) null);
    }

    public Property<String> propertyNameProperty() {
        return propertyName;
    }

    public Property<TextExpression> propertyValueProperty() {
        return propertyValue;
    }

    public Property<GameObject> gameObjectProperty() {
        return gameObject;
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aConditionResult) {
        if (!propertyValue.isNull()) {

            String thePropertyName = propertyName.get();

            ExpressionParser theParser = aScene.get(propertyValue.get());
            theParser.registerVariable(ExpressionParser.EVENT_VARIABLE, aConditionResult.getEvent());

            for (GameObjectInstance theInstance : aConditionResult.getAffectedInstances()) {

                if (theInstance.getOwnerGameObject() == gameObject.get()) {
                    theParser.registerVariable(ExpressionParser.INSTANCE_VARIABLE, theInstance);

                    PropertyDiscoverer theDiscoverer = new PropertyDiscoverer();
                    Object theOldValue = theDiscoverer.resolveVariable(theInstance, thePropertyName);

                    theParser.registerVariable(ExpressionParser.OLD_VALUE_VARIABLE, theOldValue);

                    Object theNewPropertyValue = theParser.evaluateToObject();

                    theDiscoverer.setVariable(theInstance, thePropertyName, theNewPropertyValue);
                }
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put(PROPERTY_NAME_PROPERTY, propertyName.get());
        theResult.put(PROPERTY_VALUE_PROPERTY, propertyValue.get().serialize());
        if (!gameObject.isNull()) {
            theResult.put(PROPERTY_GAMEOBJECT_PROPERTY, gameObject.get().uuidProperty().get());
        }
        return theResult;
    }

    public static SetPropertyAction unmarshall(Map<String, Object> aSerializedData, GameScene aGameScene) {
        SetPropertyAction theResult = new SetPropertyAction();
        theResult.propertyName.setQuietly((String) aSerializedData.get(PROPERTY_NAME_PROPERTY));
        Map<String, Object> theValue = (Map<String, Object>) aSerializedData.get(PROPERTY_VALUE_PROPERTY);
        if (theValue != null) {
            theResult.propertyValue.setQuietly(TextExpression.deserialize(theValue));
        }
        String theUUID = (String) aSerializedData.get(PROPERTY_GAMEOBJECT_PROPERTY);
        if (theUUID != null) {
            theResult.gameObject.setQuietly(aGameScene.findGameObjectByID(theUUID));
        }
        return theResult;
    }
}