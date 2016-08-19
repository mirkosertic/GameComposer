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
package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.event.Property;

public interface Physics {

    String ACTIVE_PROPERTY = "active";
    String FIXED_ROTATION_PROPERTY = "fixedRotation";
    String DENSITY_PROPERTY = "density";
    String FRICTION_PROPERTY = "friction";
    String RESTITUTION_PROPERTY = "restitution";
    String GRAVITY_SCALE_PROPERTY = "gravityScale";

    @ReflectiveField
    Property<Boolean> activeProperty();

    @ReflectiveField
    Property<Boolean> fixedRotationProperty();

    @ReflectiveField
    Property<Float> densityProperty();

    @ReflectiveField
    Property<Float> frictionProperty();

    @ReflectiveField
    Property<Float> restitutionProperty();

    @ReflectiveField
    Property<Float> gravityScaleProperty();

    @ReflectiveMethod
    void delete();
}
