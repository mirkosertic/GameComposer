package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.CameraBehaviorTemplate;
import de.mirkosertic.gameengine.camera.CameraClassInformation;
import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectClassInformation;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameObjectInstanceClassInformation;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.physic.PhysicsBehavior;
import de.mirkosertic.gameengine.physic.PhysicsBehaviorTemplate;
import de.mirkosertic.gameengine.physic.PhysicsClassInformation;
import de.mirkosertic.gameengine.physic.PlatformBehavior;
import de.mirkosertic.gameengine.physic.PlatformBehaviorTemplate;
import de.mirkosertic.gameengine.physic.PlatformClassInformation;
import de.mirkosertic.gameengine.physic.StaticBehavior;
import de.mirkosertic.gameengine.physic.StaticBehaviorTemplate;
import de.mirkosertic.gameengine.physic.StaticClassInformation;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehavior;
import de.mirkosertic.gameengine.playerscore.PlayerScoreBehaviorTemplate;
import de.mirkosertic.gameengine.playerscore.PlayerScoreClassInformation;
import de.mirkosertic.gameengine.sprite.SpriteBehavior;
import de.mirkosertic.gameengine.sprite.SpriteBehaviorTemplate;
import de.mirkosertic.gameengine.sprite.SpriteClassInformation;
import de.mirkosertic.gameengine.text.TextBehavior;
import de.mirkosertic.gameengine.text.TextBehaviorTemplate;
import de.mirkosertic.gameengine.text.TextClassInformation;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.type.ScoreValue;
import de.mirkosertic.gameengine.type.TextExpression;
import de.mirkosertic.gameengine.type.ValueProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PropertyDiscoverer {

    private final Map<String, Class<? extends BehaviorTemplate>> gameObjectTemplates;
    private final Map<String, Class<? extends Behavior>> gameObjectInstanceComponents;
    private final Map<Class, AutomaticResultConverter> typeConverters;

    public PropertyDiscoverer() {
        gameObjectTemplates = new HashMap<String, Class<? extends BehaviorTemplate>>();
        gameObjectTemplates.put("playerScore", PlayerScoreBehaviorTemplate.class);
        gameObjectTemplates.put("sprite", SpriteBehaviorTemplate.class);
        gameObjectTemplates.put("physics", PhysicsBehaviorTemplate.class);
        gameObjectTemplates.put("platform", PlatformBehaviorTemplate.class);
        gameObjectTemplates.put("camera", CameraBehaviorTemplate.class);
        gameObjectTemplates.put("text", TextBehaviorTemplate.class);
        gameObjectTemplates.put("static", StaticBehaviorTemplate.class);
        gameObjectInstanceComponents = new HashMap<String, Class<? extends Behavior>>();
        gameObjectInstanceComponents.put("playerScore", PlayerScoreBehavior.class);
        gameObjectInstanceComponents.put("sprite", SpriteBehavior.class);
        gameObjectInstanceComponents.put("physics", PhysicsBehavior.class);
        gameObjectInstanceComponents.put("platform", PlatformBehavior.class);
        gameObjectInstanceComponents.put("camera", CameraBehavior.class);
        gameObjectInstanceComponents.put("text", TextBehavior.class);
        gameObjectInstanceComponents.put("static", StaticBehavior.class);
        typeConverters = new HashMap<Class, AutomaticResultConverter>();
        typeConverters.put(Angle.class, new ToAngleResultConverter());
        typeConverters.put(ResourceName.class, new ToResourceNameConverter());
        typeConverters.put(TextExpression.class, new ToTextExpressionConverter());
        typeConverters.put(ScoreValue.class, new ToScoreValueResultConverter());
        typeConverters.put(String.class, new ToStringExpressionConverter());
        typeConverters.put(Integer.class, new ToIntegerConverter());
        typeConverters.put(Long.class, new ToLongConverter());
        typeConverters.put(Float.class, new ToFloatConverter());
        typeConverters.put(Boolean.class, new ToBooleanConverter());
    }

    private Object resolveProperty(Object aObject, String aPropertyName) {
        if (aObject instanceof GameObject) {
            GameObject theObject = (GameObject) aObject;
            Class<? extends BehaviorTemplate> theTemplateClass = gameObjectTemplates.get(aPropertyName);
            if (theTemplateClass != null) {
                BehaviorTemplate theTemplate = theObject.getComponentTemplate(theTemplateClass);
                if (theTemplate != null) {
                    return theTemplate;
                }
            }
        }
        if (aObject instanceof GameObjectInstance) {
            GameObjectInstance theObject = (GameObjectInstance) aObject;
            Class<? extends Behavior> theTemplateClass = gameObjectInstanceComponents.get(aPropertyName);
            if (theTemplateClass != null) {
                Behavior theComponent = theObject.getComponent(theTemplateClass);
                if (theComponent != null) {
                    return theComponent;
                }
            }
        }
        if (aObject instanceof Reflectable) {
            Reflectable theReflectable = (Reflectable) aObject;
            ClassInformation theClassInformation = theReflectable.getClassInformation();
            Field theField = theClassInformation.getFieldByName(aPropertyName);
            if (theField != null) {
                return theField.getValue(aObject);
            }
            return null;
        }
        if (aObject instanceof Map) {
            Map<String, Object> theMap = (Map<String, Object>) aObject;
            Object theValue = theMap.get(aPropertyName);
            if (theValue instanceof ValueProvider) {
                return ((ValueProvider) theValue).get();
            }
            return theValue;
        }

        return null;
    }

    public Object resolveVariable(Object aObject, String aVariableName) {
        int p = aVariableName.indexOf('.');
        if (p > 0) {
            String theName = aVariableName.substring(0, p);
            String thePropertyName = aVariableName.substring(p + 1);
            Object theProperyValue = resolveProperty(aObject, theName);
            if (theProperyValue != null) {
                return resolveVariable(theProperyValue, thePropertyName);
            }
            return null;
        }

        Object theValue = resolveProperty(aObject, aVariableName);
        while (theValue instanceof ValueProvider) {
            theValue = ((ValueProvider) theValue).get();
        }
        return theValue;
    }

    private void addProperties(ClassInformation aClassInformation, String aPrefix, Set<String> aPropertyList) {
        for (Field theField : aClassInformation.getFields()) {
            if (aPrefix == null) {
                aPropertyList.add(theField.getName());
            } else {
                aPropertyList.add(aPrefix + "." + theField.getName());
            }
        }
    }

    public List<String> gatherAllPossibleProperties() {
        Set<String> theProperties = new HashSet<String>();
        addProperties(GameObjectClassInformation.INSTANCE, null, theProperties);
        addProperties(GameObjectInstanceClassInformation.INSTANCE, null, theProperties);
        addProperties(PlayerScoreClassInformation.INSTANCE, "playerScore", theProperties);
        addProperties(SpriteClassInformation.INSTANCE, "sprite", theProperties);
        addProperties(PhysicsClassInformation.INSTANCE, "physics", theProperties);
        addProperties(PlatformClassInformation.INSTANCE, "platform", theProperties);
        addProperties(CameraClassInformation.INSTANCE, "camera", theProperties);
        addProperties(TextClassInformation.INSTANCE, "text", theProperties);
        addProperties(StaticClassInformation.INSTANCE, "static", theProperties);
        List<String> theResult = new ArrayList<String>(theProperties);
        Collections.sort(theResult);
        return theResult;
    }

    public <T> T convert(Object aValue, Class<T> aType) {
        AutomaticResultConverter theConverter = typeConverters.get(aType);
        if (theConverter != null) {
            return (T) theConverter.convert(aValue);
        }
        return (T) aValue;
    }

    public void setVariable(Object aInstance, String aPropertyName, Object aValue) {
        int p = aPropertyName.indexOf('.');
        if (p>0) {
            String thePropertyName = aPropertyName.substring(0, p);
            String theSuffix = aPropertyName.substring(p+1);

            Object theValue = resolveProperty(aInstance, thePropertyName);
            if (theValue != null) {
                setVariable(theValue, theSuffix, aValue);
            } else {
                throw new IllegalArgumentException("Cannot get property "+thePropertyName+" on "+theValue);
            }
        } else {
            Object theValue = resolveProperty(aInstance, aPropertyName);
            if (theValue instanceof Property) {
                Property theProperty = (Property) theValue;
                theProperty.set(convert(aValue, theProperty.getType()));
            } else {
                throw new IllegalArgumentException("Cannot set property "+aPropertyName+" on "+theValue+" to "+aValue);
            }
        }
    }
}
