package de.mirkosertic.gameengine.gwt;

import com.google.gwt.i18n.client.DateTimeFormat;
import de.mirkosertic.gameengine.expression.BuiltInFunctions;

import java.sql.Timestamp;

public class GWTBuiltInFunctions extends BuiltInFunctions {

    @Override
    public String formatTime(Number aTimeInMilis, String aPattern) {
        DateTimeFormat theFormat = DateTimeFormat.getFormat(aPattern);
        return theFormat.format(new Timestamp(aTimeInMilis.longValue()));
    }
}
