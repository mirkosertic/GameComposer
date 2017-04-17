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
package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.HashMap;
import java.util.Map;

public class CameraBehaviorTemplate implements BehaviorTemplate<CameraBehavior>, Camera, Reflectable<CameraClassInformation> {

    public static final BehaviorType TYPE = new BehaviorType("CameraBehaviorTemplate");

    private static final CameraClassInformation CIINSTANCE = new CameraClassInformation();

    private final Property<CameraType> type;
    private final GameObject owner;

    public CameraBehaviorTemplate(GameEventManager aEventManager, GameObject aOwner) {
        type = new Property<>(CameraType.class, this, TYPE_PROPERTY, CameraType.FOLLOWPLAYER, aEventManager);
        owner = aOwner;
    }

    @Override
    public BehaviorType getType() {
        return TYPE;
    }

    @Override
    public CameraClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    @Override
    public Property<CameraType> typeProperty() {
        return type;
    }

    @Override
    public CameraBehavior create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        final CameraBehavior theResult = new CameraBehavior(aInstance, this);
        theResult.registerEvents(aGameRuntime);
        return theResult;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(CameraBehavior.TYPE_ATTRIBUTE, CameraBehavior.TYPE);
        theResult.put("cameratype", type.get().name());
        return theResult;
    }

    @Override
    public void delete() {
        owner.getGameScene().removeBehaviorFrom(owner, this);
    }

    public static CameraBehaviorTemplate deserialize(GameEventManager aEventManager, GameObject aOwner, Map<String, Object> aSerializedData) {
        CameraBehaviorTemplate theTemplate = new CameraBehaviorTemplate(aEventManager, aOwner);
        String theCameraType = (String) aSerializedData.get("cameratype");
        if (theCameraType != null) {
            theTemplate.type.setQuietly(CameraType.valueOf(theCameraType));
        }
        return theTemplate;
    }
}
