package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.type.BuiltInFunctions;
import de.mirkosertic.gameengine.type.BuiltInFunctionsClassInformation;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TeaVMBuildInFunctions extends BuiltInFunctions {

    private static final BuiltInFunctionsClassInformation CIINSTANCE = new BuiltInFunctionsClassInformation();

    @Override
    public String formatTime(Number aTimeInMilis, String aPattern) {
        SimpleDateFormat theFormat = new SimpleDateFormat(aPattern);
        return theFormat.format(new Date(aTimeInMilis.longValue()));
    }

    @Override
    public BuiltInFunctionsClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
