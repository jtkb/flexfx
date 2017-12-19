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

public final class OsgiFxTestConstants {

    public static final String OSGIFX_GROUP_ID = "com.javatechnics.flexfx";
    public static final String OSGIFX_BOOT_ARTIFACT_ID = "boot";
    public static final String KARAF_VERSION = "4.0.6";
    public static final String BUNDLE_INSTALL_ACL_CFG = "etc/org.apache.karaf.command.acl.bundle.cfg";
    public static final String CONFIG_PROPERTIES = "etc/config.properties";
    public static final String IT_DUMMY_BUNDLE_ARTIFACT_ID = "dummy";
    public static final String DUMMY_TWO_BUNDLE_ARTIFACT_ID = "dummy-two";
    public static final String PROJECT_VERSION = "1.0.0-SNAPSHOT";
    public static final String JAVATECHNICS_TESTFX_GROUP_ID = "com.javatechnics.org.testfx";
    public static final String TESTFX_GROUP_ID =  System.getProperty("java.version").startsWith("1.8")
            ? JAVATECHNICS_TESTFX_GROUP_ID :"org.testfx";
    public static final String TESTFX_CORE_ARTIFACT_ID = "testfx-core";
    public static final String TESTFX_INTERNAL_JAVA8_ARTIFACT_ID = "testfx-internal-java8";
    public static final String TESTFX_INTERNAL_JAVA9_ARTIFACT_ID = "testfx-internal-java9";
    public static final String TESTFX_INTERNAL_ARTIFACT_ID = System.getProperty("java.version").startsWith("1.8")
            ? TESTFX_INTERNAL_JAVA8_ARTIFACT_ID : TESTFX_INTERNAL_JAVA9_ARTIFACT_ID;
    public static final String TESTFX_VERSION = "4.0.8-alpha";
    public static final MavenArtifactUrlReference KARAF_URL = maven()
            .artifactId("apache-karaf")
            .groupId("org.apache.karaf")
            .version(KARAF_VERSION)
            .type("tar.gz");

}
