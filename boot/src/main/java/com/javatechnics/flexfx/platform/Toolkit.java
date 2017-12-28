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

package com.javatechnics.flexfx.platform;

/**
 * Implement and offered as an OSGi service when the JavaFX thread has been started. Client
 * bundles can take the availability of this service as a guarantee that methods that rely on the
 * JavaFX toolkit are safe to call.
 */
@FunctionalInterface
public interface Toolkit
{
    Boolean isReady();

}
