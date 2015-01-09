package de.mirkosertic.gameengine.gwt;

import com.google.gwt.i18n.client.DateTimeFormat;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.type.BuiltInFunctions;

import java.sql.Timestamp;

@InheritedClassInformation
public class GWTBuiltInFunctions extends BuiltInFunctions {

    @ReflectiveMethod
    @Override
    public String formatTime(Number aTimeInMilis, String aPattern) {
        DateTimeFormat theFormat = DateTimeFormat.getFormat(aPattern);
        return theFormat.format(new Timestamp(aTimeInMilis.longValue()));
    }

    @Override
    public GWTBuiltInFunctionsClassInformation getClassInformation() {
        return GWTBuiltInFunctionsClassInformation.INSTANCE;
    }
}
