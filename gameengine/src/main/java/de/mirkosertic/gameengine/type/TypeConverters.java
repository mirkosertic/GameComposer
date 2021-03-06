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

import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

public class TypeConverters implements Reflectable<TypeConvertersClassInformation> {

    private static final TypeConvertersClassInformation CIINSTANCE = new TypeConvertersClassInformation();

    @Override
    public TypeConvertersClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @ReflectiveMethod
    public TextExpression textexpression(String aTextExpression) {
        return new TextExpression(aTextExpression);
    }
}
