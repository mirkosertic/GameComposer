package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

public class TypeConverters implements Reflectable<TypeConvertersClassInformation> {

    private static final TypeConvertersClassInformation CIINSTANCE = new TypeConvertersClassInformation();

    @Override
    public TypeConvertersClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @ReflectiveMethod
    public TextExpression textexpression(String aTextExpression) {
        return new TextExpression(aTextExpression);
    }
}
