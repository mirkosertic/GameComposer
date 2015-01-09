package de.mirkosertic.gameengine.android;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.type.BuiltInFunctions;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@InheritedClassInformation
public class JDKBuiltInFunctions extends BuiltInFunctions {

    @ReflectiveMethod
    @Override
    public String formatTime(Number aTimeInMilis, String aPattern) {
        SimpleDateFormat theFormat = new SimpleDateFormat(aPattern);
        return theFormat.format(new Timestamp(aTimeInMilis.longValue()));
    }

    @Override
    public JDKBuiltInFunctionsClassInformation getClassInformation() {
        return JDKBuiltInFunctionsClassInformation.INSTANCE;
    }
}
