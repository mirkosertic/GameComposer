package de.mirkosertic.gameengine.scriptengine.luaj;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngine;
import de.mirkosertic.gameengine.type.TypeConverters;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;
import de.mirkosertic.gameengine.type.Reflectable;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaDouble;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

public class LuaJScriptEngine implements LUAScriptEngine {

    private static LuaValue toLuaValue(Object aValue) {
        if (aValue == null) {
            return LuaValue.NIL;
        }
        if (aValue instanceof Reflectable) {
            return toLuaValue((Reflectable) aValue);
        }
        if (aValue instanceof Double) {
            return LuaDouble.valueOf((double) aValue);
        }
        if (aValue instanceof Float) {
            return LuaDouble.valueOf((float) aValue);
        }
        if (aValue instanceof Integer) {
            return LuaInteger.valueOf((int) aValue);
        }
        if (aValue instanceof Long) {
            return LuaInteger.valueOf((long) aValue);
        }
        if (aValue instanceof String) {
            return LuaString.valueOf((String) aValue);
        }

        // Objects are converted to plain tables
        LuaTable theTable = new LuaTable();
        theTable.set("javaobject", LuaValue.userdataOf(aValue));
        return theTable;
    }

    static void registerTo(LuaTable aTable, Reflectable aObject) {
        ClassInformation theClassInformation = aObject.getClassInformation();
        for (Field theField : theClassInformation.getFields()) {
            aTable.set(theField.getName(), new FieldAccessFunction(aObject, theField));
        }
        for (Method theMethod : theClassInformation.getMethods()) {
            aTable.set(theMethod.getName(), new MethodInvocationFunction(aObject, theMethod));
        }
    }

    private static LuaValue toLuaValue(Reflectable aObject) {
        LuaTable theTable = new LuaTable();
        theTable.set("javaobject", LuaValue.userdataOf(aObject));
        registerTo(theTable, aObject);
        return theTable;
    }

    private static Object toJavaValue(LuaValue aValue, Class aTargetClass) {
        if (aTargetClass == Number.class) {
            if (aValue.islong()) {
                return aValue.tolong();
            }
            if (aValue.isint()) {
                return aValue.toint();
            }
            throw new IllegalArgumentException("Cannot convert " + aValue +" to " + aTargetClass);
        }
        if (aTargetClass == String.class) {
            return aValue.toString();
        }
        if (aTargetClass == Integer.class) {
            return aValue.toint();
        }
        if (aTargetClass == Long.class) {
            return aValue.tolong();
        }
        if (aTargetClass == Float.class) {
            return aValue.tofloat();
        }
        if (aTargetClass == Double.class) {
            return aValue.todouble();
        }
        throw new IllegalArgumentException("Cannot convert " + aValue+" to " + aTargetClass);
    }

    private static Object toJavaValue(LuaValue aValue) {
        if (aValue.istable()) {
            LuaTable theTable = (LuaTable) aValue;
            LuaValue theObject = theTable.get("javaobject");
            if (theObject.isuserdata()) {
                return theObject.checkuserdata();
            }
            throw new IllegalArgumentException("Cannot convert " + aValue+" to java object");
        }
        if (aValue.isnil()) {
            return null;
        }
        if (aValue.islong()) {
            return aValue.tolong();
        }
        return aValue.toString();
    }

    private static class FieldAccessFunction extends VarArgFunction {

        private final Object object;
        private final Field field;

        public FieldAccessFunction(Object aObject, Field aField) {
            object = aObject;
            field = aField;
        }

        @Override
        public Varargs invoke(Varargs aArgs) {
            if ((field.getType() == Property.class) || (field.getType() == ReadOnlyProperty.class)) {
                ReadOnlyProperty theProperty = (ReadOnlyProperty) field.getValue(object);
                // If zero arg, it is a property read access
                if (aArgs.narg() == 0) {
                    return toLuaValue(theProperty.get());
                }
                if (aArgs.narg() != 1) {
                    throw new IllegalArgumentException("Only one argument supported to set property value, got " + aArgs.narg()+" arguments");
                }
                if (field.getType() == Property.class) {
                    ((Property) theProperty).set(toJavaValue(aArgs.arg(1)));
                } else {
                    throw new IllegalArgumentException("Cannot set read-only properties");
                }
                return LuaValue.NIL;
            }
            // Normal field, we just support read access
            if (aArgs.narg() != 0) {
                throw new IllegalArgumentException("Field is read only, but got " + aArgs.narg()+" arguments");
            }
            return toLuaValue(field.getValue(object));
        }
    }

    private static class MethodInvocationFunction extends VarArgFunction {

        private final Object object;
        private final Method method;

        public MethodInvocationFunction(Object aObject, Method aMethod) {
            object = aObject;
            method = aMethod;
        }

        @Override
        public Varargs invoke(Varargs aArguments) {
            if (aArguments.narg() == method.getArgument().length) {
                Object[] theArguments = new Object[aArguments.narg()];
                for (int i = 1; i <= aArguments.narg(); i++) {
                    theArguments[i-1] = toJavaValue(aArguments.arg(i), method.getArgument()[i-1]);
                }
                return toLuaValue(method.invoke(object, theArguments));
            }
            throw new IllegalArgumentException(method.getArgument().length+" arguments required for " + method.getName());
        }
    }

    private final Globals globals;
    private final LuaClosure closure;
    private final LuaValue methodToCall;

    public LuaJScriptEngine(Globals aGlobals, LuaClosure aClosure, String aMethodName) {
        globals = aGlobals;
        closure = aClosure;

        // Initialize the code
        closure.call();

        // Retrieve function from globals
        methodToCall = globals.get(aMethodName);
        if (methodToCall == null) {
            throw new IllegalStateException("No "+aMethodName+" function defined");
        }

        // Register generic Type converters
        registerTo(globals, new TypeConverters());
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void registerObject(String aObjectName, Reflectable aObject) {
        if (aObject != null) {
            globals.set(aObjectName, toLuaValue(aObject));
        }
    }

    @Override
    public void registerPrimitive(String aObjectName, long aValue) {
        globals.set(aObjectName, LuaInteger.valueOf(aValue));
    }

    @Override
    public Object proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {

        Varargs theArguments = LuaValue.varargsOf(new LuaValue[] {
            LuaInteger.valueOf(aGameTime),
            LuaInteger.valueOf(aElapsedTimeSinceLastLoop)
        });

        Varargs theResult = methodToCall.invoke(theArguments);
        if (theResult.narg() == 1) {
            return toJavaValue(theResult.arg(1));
        }
        throw new IllegalStateException("Not supported return type : " + theResult);
    }

    @Override
    public String evaluateSimpleExpressionFor(GameObjectInstance aObjectInstance) {
        Varargs theArguments = LuaValue.varargsOf(new LuaValue[] {
                    toLuaValue(aObjectInstance)
            });
        Varargs theResult = methodToCall.invoke(theArguments);
        if (theResult.narg() == 1) {
            return toJavaValue(theResult.arg(1)).toString();
        }
        throw new IllegalStateException("Invalid return type : " + theResult);
    }
}