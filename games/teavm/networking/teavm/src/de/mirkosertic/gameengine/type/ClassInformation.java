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
package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;

public abstract class ClassInformation {

    private final Map<String, Field> fields;
    private final Map<String, Method> methods;

    protected ClassInformation() {
        fields = new HashMap<String, Field>();
        methods = new HashMap<String, Method>();
    }

    protected void register(Field aField) {
        fields.put(aField.getName(), aField);
    }

    protected void register(Method aMethod) {
        methods.put(aMethod.getName(), aMethod);
    }

    public Field[] getFields() {
        return fields.values().toArray(new Field[fields.size()]);
    }

    public Field getFieldByName(String aFieldName) {
        return fields.get(aFieldName);
    }

    public Method getMethodByName(String aMethodName) {
        return methods.get(aMethodName);
    }

    public Method[] getMethods() {
        return methods.values().toArray(new Method[methods.size()]);
    }
}