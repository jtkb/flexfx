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

package com.javatechnics.flexfx;

import org.ops4j.pax.exam.options.MavenArtifactUrlReference;

import static org.ops4j.pax.exam.CoreOptions.maven;

public final class FlexFxTestConstants
{
    public static final String FLEXFX_GROUP_ID = "${flexfx.groupId}";
    public static final String FLEXFX_BOOT_ARTIFACT_ID = "${flexfx.artifactId}";
    public static final String FLEXFX_VERSION = "${flexfx.version}";
    public static final String KARAF_VERSION = "${karaf.version}";
    public static final String BUNDLE_INSTALL_ACL_CFG = "etc/org.apache.karaf.command.acl.bundle.cfg";
    public static final String IT_DUMMY_BUNDLE_ARTIFACT_ID = "dummy";
    public static final String DUMMY_TWO_BUNDLE_ARTIFACT_ID = "dummy-two";
    public static final String TESTFX_GROUP_ID =  "${testfx.groupId}";
    public static final String TESTFX_CORE_ARTIFACT_ID = "${testfx.core.artifactId}";
    public static final String TESTFX_INTERNAL_ARTIFACT_ID = "${testfx.internal.artifactId}";
    public static final String TESTFX_VERSION = "${testfx.version}";
    public static final String PAX_EXAM_UNPACK_DIR = "${pax.exam.unpack.dir}";
    public static final String KARAF_CFG_FILE = "${karaf.cfg.file}";
    public static final String SUN_GLASS_UI_PACKAGE = "${sun.glass.ui.package}";
    public static final MavenArtifactUrlReference KARAF_URL = maven()
            .artifactId("apache-karaf")
            .groupId("org.apache.karaf")
            .version(KARAF_VERSION)
            .type("tar.gz");
}
