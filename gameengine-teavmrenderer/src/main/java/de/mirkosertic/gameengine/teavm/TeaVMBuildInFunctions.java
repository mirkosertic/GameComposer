package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.expression.BuiltInFunctions;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TeaVMBuildInFunctions extends BuiltInFunctions {

    @Override
    public String formatTime(Number aTimeInMilis, String aPattern) {
        SimpleDateFormat theFormat = new SimpleDateFormat(aPattern);
        return theFormat.format(new Timestamp(aTimeInMilis.longValue()));
    }
}
