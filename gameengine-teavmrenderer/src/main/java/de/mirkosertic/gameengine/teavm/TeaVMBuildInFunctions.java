package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.expression.BuiltInFunctions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TeaVMBuildInFunctions extends BuiltInFunctions {

    @Override
    public String formatTime(Number aTimeInMilis, String aPattern) {
        SimpleDateFormat theFormat = new SimpleDateFormat(aPattern);
        return theFormat.format(new Date(aTimeInMilis.longValue()));
    }
}
