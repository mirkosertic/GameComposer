package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.type.BuiltInFunctions;
import de.mirkosertic.gameengine.type.BuiltInFunctionsClassInformation;

import java.sql.Timestamp;
import com.google.gwt.i18n.client.DateTimeFormat;

public class GWTBuiltInFunctions extends BuiltInFunctions {

    @Override
    public String formatTime(Number aTimeInMilis, String aPattern) {
        DateTimeFormat theFormat = DateTimeFormat.getFormat(aPattern);
        return theFormat.format(new Timestamp(aTimeInMilis.longValue()));
    }

    @Override
    public BuiltInFunctionsClassInformation getClassInformation() {
        return BuiltInFunctionsClassInformation.INSTANCE;
    }
}
