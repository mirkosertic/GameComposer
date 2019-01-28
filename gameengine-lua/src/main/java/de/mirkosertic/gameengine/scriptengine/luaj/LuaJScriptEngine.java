/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.scriptengine.luaj;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngine;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.KeyValueObjectCache;
import de.mirkosertic.gameengine.type.Method;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.TypeConverters;

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

    private static LuaValue toLuaValue(Object aValue, KeyValueObjectCache aCache) {
        if (aValue == null) {
            return LuaValue.NIL;
        }
        if (aValue instanceof Reflectable) {
            return toLuaValue((Reflectable) aValue, aCache);
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
        theTable.set(LuaString.valueOf("javaobject"), LuaValue.userdataOf(aValue));
        return theTable;
    }

    static void registerTo(LuaTable aTable, Reflectable aObject, KeyValueObjectCache aCache) {
        ClassInformation theClassInformation = aObject.getClassInformation();
        for (Field theField : theClassInformation.getFields()) {
            aTable.set(LuaString.valueOf(theField.getName()), new FieldAccessFunction(aCache, aObject, theField));
        }
        for (Method theMethod : theClassInformation.getMethods()) {
            aTable.set(LuaString.valueOf(theMethod.getName()), new MethodInvocationFunction(aCache, aObject, theMethod));
        }
    }

    private static LuaValue toLuaValue(Reflectable aObject, KeyValueObjectCache aCache) {
        LuaTable theTable = aCache.getObjectForKey(aObject);
        if (theTable == null) {
            theTable = new LuaTable();
            theTable.set(LuaString.valueOf("javaobject"), LuaValue.userdataOf(aObject));
            registerTo(theTable, aObject, aCache);

            aCache.setObjectForKey(aObject, theTable);
        }
        return theTable;
    }

    private static Object toJavaValue(LuaValue aValue, Class aTargetClass) {
        if (aValue.isnil()) {
            return null;
        }
        if (aValue.istable()) {
            LuaTable theTable = (LuaTable) aValue;
            LuaValue theObject = theTable.get(LuaString.valueOf("javaobject"));
            if (theObject.isuserdata()) {
                return theObject.checkuserdata();
            }
            throw new IllegalArgumentException("Cannot convert " + aValue+" to java object");
        }
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
        if (aTargetClass == Object.class) {
            if (aValue.isint()) {
                return aValue.toint();
            }
            if (aValue.islong()) {
                return aValue.tolong();
            }
        }
        throw new IllegalArgumentException("Cannot convert " + aValue+" to " + aTargetClass);
    }

    private static Object toJavaValue(LuaValue aValue) {
        if (aValue.isnil()) {
            return null;
        }
        if (aValue.istable()) {
            LuaTable theTable = (LuaTable) aValue;
            LuaValue theObject = theTable.get(LuaString.valueOf("javaobject"));
            if (theObject.isuserdata()) {
                return theObject.checkuserdata();
            }
            throw new IllegalArgumentException("Cannot convert " + aValue+" to java object");
        }
        if (aValue.isint()) {
            return aValue.toint();
        }
        if (aValue.islong()) {
            return aValue.tolong();
        }
        if (aValue.isstring()) {
            return aValue.toString();
        }
        return aValue.toString();
    }

    private static class FieldAccessFunction extends VarArgFunction {

        private final Object object;
        private final Field field;
        private final KeyValueObjectCache cache;

        public FieldAccessFunction(KeyValueObjectCache aCache, Object aObject, Field aField) {
            object = aObject;
            field = aField;
            cache = aCache;
        }

        @Override
        public Varargs invoke(Varargs aArgs) {
            if ((field.getType() == Property.class) || (field.getType() == ReadOnlyProperty.class)) {
                ReadOnlyProperty theProperty = (ReadOnlyProperty) field.getValue(object);
                // If zero arg, it is a property read access
                if (aArgs.narg() == 0) {
                    return toLuaValue(theProperty.get(), cache);
                }
                if (aArgs.narg() != 1) {
                    throw new IllegalArgumentException("Only one argument supported to set property value, got " + aArgs.narg()+" arguments");
                }
                if (field.getType() == Property.class) {
                    ((Property) theProperty).set(toJavaValue(aArgs.arg(1), theProperty.getType()));
                } else {
                    throw new IllegalArgumentException("Cannot set read-only properties");
                }
                return LuaValue.NIL;
            }
            // Normal field, we just support read access
            if (aArgs.narg() != 0) {
                throw new IllegalArgumentException("Field is read only, but got " + aArgs.narg()+" arguments");
            }
            return toLuaValue(field.getValue(object), cache);
        }
    }

    private static class MethodInvocationFunction extends VarArgFunction {

        private final Object object;
        private final Method method;
        private final KeyValueObjectCache cache;

        public MethodInvocationFunction(KeyValueObjectCache aCache, Object aObject, Method aMethod) {
            object = aObject;
            method = aMethod;
            cache = aCache;
        }

        @Override
        public Varargs invoke(Varargs aArguments) {
            if (aArguments.narg() == method.getArgument().length) {
                Object[] theArguments = new Object[aArguments.narg()];
                for (int i = 1; i <= aArguments.narg(); i++) {
                    theArguments[i-1] = toJavaValue(aArguments.arg(i), method.getArgument()[i-1]);
                }
                return toLuaValue(method.invoke(object, theArguments), cache);
            }
            throw new IllegalArgumentException(method.getArgument().length+" arguments required for " + method.getName());
        }
    }

    private final Globals globals;
    private final KeyValueObjectCache cache;
    private final String methodName;

    public LuaJScriptEngine(KeyValueObjectCache aCache, Globals aGlobals, String aMethodName) {
        globals = aGlobals;
        cache = aCache;
        methodName = aMethodName;

        // Register generic Type converters
        registerTo(globals, new TypeConverters(), cache);
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void registerObject(String aObjectName, Reflectable aObject) {
        if (aObject != null) {
            globals.set(LuaString.valueOf(aObjectName), toLuaValue(aObject, cache));
        }
    }

    @Override
    public void registerPrimitive(String aObjectName, long aValue) {
        globals.set(LuaString.valueOf(aObjectName), LuaInteger.valueOf(aValue));
    }

    @Override
    public Object proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {

        Varargs theArguments = LuaValue.varargsOf(new LuaValue[] {
            LuaInteger.valueOf(aGameTime),
            LuaInteger.valueOf(aElapsedTimeSinceLastLoop)
        });

        LuaClosure theMethodToCall = (LuaClosure) globals.get(LuaString.valueOf(methodName));

        Varargs theResult = theMethodToCall.invoke(theArguments);
        if (theResult.narg() == 1) {
            return toJavaValue(theResult.arg(1));
        }
        throw new IllegalStateException("Not supported return type : " + theResult + " for function " + methodName);
    }

    @Override
    public String evaluateSimpleExpressionFor(GameObjectInstance aObjectInstance) {
        Varargs theArguments = LuaValue.varargsOf(new LuaValue[] {
                    toLuaValue(aObjectInstance, cache),
                    toLuaValue(aObjectInstance.getOwnerGameObject().getGameScene(), cache),
                    toLuaValue(aObjectInstance.getOwnerGameObject().getGameScene().getGame(), cache),
            });

        LuaClosure theMethodToCall = (LuaClosure) globals.get(LuaString.valueOf(methodName));

        Varargs theResult = theMethodToCall.invoke(theArguments);
        if (theResult.narg() == 1) {
            return toJavaValue(theResult.arg(1), String.class).toString();
        }
        throw new IllegalStateException("Invalid return type : " + theResult);
    }
}