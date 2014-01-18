package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Method;

public class BuiltInFunctionsClassInformation extends ClassInformation {

    public static final Method<BuiltInFunctions> MAX_METHOD = new Method<BuiltInFunctions>("max",Number.class,new Class[] {Number.class, Number.class}) {
        @Override
        public Object invoke(BuiltInFunctions aTarget, Object[] aArguments) {
            return aTarget.max((Number) aArguments[0], (Number) aArguments[1]);
        }
    };

    public static final Method<BuiltInFunctions> MIN_METHOD = new Method<BuiltInFunctions>("min",Number.class,new Class[] {Number.class, Number.class}) {
        @Override
        public Object invoke(BuiltInFunctions aTarget, Object[] aArguments) {
            return aTarget.min((Number) aArguments[0], (Number) aArguments[1]);
        }
    };

    public static final Method<BuiltInFunctions> SYSTIME_METHOD = new Method<BuiltInFunctions>("systime",Number.class,new Class[] {}) {
        @Override
        public Object invoke(BuiltInFunctions aTarget, Object[] aArguments) {
            return aTarget.systime();
        }
    };

    public static final Method<BuiltInFunctions> FORMATTIME_METHOD = new Method<BuiltInFunctions>("formattime",String.class,new Class[] {Number.class, String.class}) {
        @Override
        public Object invoke(BuiltInFunctions aTarget, Object[] aArguments) {
            return aTarget.formatTime((Number) aArguments[0], (String) aArguments[1]);
        }
    };

    public static final BuiltInFunctionsClassInformation INSTANCE = new BuiltInFunctionsClassInformation();

    private BuiltInFunctionsClassInformation() {
        register(MAX_METHOD);
        register(MIN_METHOD);
        register(SYSTIME_METHOD);
        register(FORMATTIME_METHOD);
    }
}
