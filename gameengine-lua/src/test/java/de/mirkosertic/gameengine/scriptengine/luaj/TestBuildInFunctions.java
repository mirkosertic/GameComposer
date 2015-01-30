package de.mirkosertic.gameengine.scriptengine.luaj;

import de.mirkosertic.gameengine.type.BuiltInFunctions;
import de.mirkosertic.gameengine.type.BuiltInFunctionsClassInformation;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TestBuildInFunctions extends BuiltInFunctions {

    private static final BuiltInFunctionsClassInformation CIINSTANCE = new BuiltInFunctionsClassInformation();

    @Override
    public String formatTime(Number aTimeInMilis, String aPattern) {
        SimpleDateFormat theFormat = new SimpleDateFormat(aPattern);
        return theFormat.format(new Timestamp(aTimeInMilis.longValue()));
    }

    @Override
    public BuiltInFunctionsClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
