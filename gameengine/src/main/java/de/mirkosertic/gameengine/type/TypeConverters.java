package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

public class TypeConverters implements Reflectable<TypeConvertersClassInformation> {

    @Override
    public TypeConvertersClassInformation getClassInformation() {
        return TypeConvertersClassInformation.INSTANCE;
    }

    @ReflectiveMethod
    public TextExpression textexpression(String aTextExpression) {
        return new TextExpression(aTextExpression);
    }
}
