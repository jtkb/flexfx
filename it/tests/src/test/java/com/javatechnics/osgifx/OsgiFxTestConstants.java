package com.javatechnics.osgifx;

import org.ops4j.pax.exam.options.MavenArtifactUrlReference;

import static org.ops4j.pax.exam.CoreOptions.maven;

public final class OsgiFxTestConstants {

    public static final String OSGIFX_GROUP_ID = "com.javatechnics.osgifx";

    public static final String OSGIFX_BOOT_ARTIFACT_ID = "boot";

    public static final String KARAF_VERSION = "4.0.6";

    public static final String BUNDLE_INSTALL_ACL_CFG = "etc/org.apache.karaf.command.acl.bundle.cfg";

    public static final String IT_DUMMY_BUNDLE_ARTIFACT_ID = "dummy";

    public static final String PROJECT_VERSION = "1.0.0-SNAPSHOT";

    public static final MavenArtifactUrlReference KARAF_URL = maven()
            .artifactId("apache-karaf")
            .groupId("org.apache.karaf")
            .version(KARAF_VERSION)
            .type("tar.gz");

}
