/*
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 *    Copyright Kerry Billingham - 2017
 *    @author Kerry Billingham
 */

package com.javatechnics.flexfx.examples.simplenodeservice.controls;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Package private interface implemented by the Controls' OSGi-aware class so that the JavaFx thread aware
 * controller can notify control changes.
 */
@FunctionalInterface
public interface ControlsCallback
{
    String SPEED_EVENT = "SPEED_EVENT";
    String TOPIC = "animatednode/controls";
    String SPEED_SUB_TOPIC = TOPIC + "/speed";
    String BLEND_SUB_TOPIC = TOPIC + "/blend";

    enum NodeEvent
    {
        SPEED, BLEND
    }

    enum EffectType
    {
        BLUR("blur"), BLOOM("bloom"), GLOW("glow"), UNKNOWN("unknown");
        private final String type;
        private final static Map<String, EffectType> map = new HashMap<>(EffectType.values().length);

        static {
            for (EffectType type : EffectType.values()) {
                map.put(type.type, type);
            }
        }

        EffectType(final String type)
        {
            this.type = type;
        }

        /**
         * Get the EffectType for the specified string.
         *
         * @param effectType the string representing the EfectType.
         * @return the EffectType of EffectType.UNKNOWN is the specified string cannot be mapped to an Enum.
         */
        public EffectType getEffectType(final String effectType)
        {
            return Optional.ofNullable(map.get(effectType)).orElse(UNKNOWN);
        }
    }

    /**
     * Implementations are notified of control events via this method.
     *
     * @param nodeEvent the type of control event.
     * @param value     the value of the control event.
     */
    void event(NodeEvent nodeEvent, Object value);
}
