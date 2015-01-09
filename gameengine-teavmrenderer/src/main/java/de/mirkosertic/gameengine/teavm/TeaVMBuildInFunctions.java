package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.type.BuiltInFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;

@InheritedClassInformation
public class TeaVMBuildInFunctions extends BuiltInFunctions {

    @ReflectiveMethod
    @Override
    public String formatTime(Number aTimeInMilis, String aPattern) {
        SimpleDateFormat theFormat = new SimpleDateFormat(aPattern);
        return theFormat.format(new Date(aTimeInMilis.longValue()));
    }

    @Override
    public TeaVMBuildInFunctionsClassInformation getClassInformation() {
        return TeaVMBuildInFunctionsClassInformation.INSTANCE;
    }
}
