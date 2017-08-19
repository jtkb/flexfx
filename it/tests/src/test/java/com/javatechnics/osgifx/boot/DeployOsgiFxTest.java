package com.javatechnics.osgifx.boot;

import org.apache.karaf.features.BootFinished;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.api.console.SessionFactory;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static com.javatechnics.osgifx.OsgiFxTestConstants.*;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*;

@ExamReactorStrategy(PerClass.class)
@RunWith(PaxExam.class)
public class DeployOsgiFxTest {

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private PrintStream printStream = new PrintStream(byteArrayOutputStream);
    private PrintStream errStream = new PrintStream(byteArrayOutputStream);
    private Session session;

    @Inject
    protected BootFinished bootFinished;

    @Inject
    protected SessionFactory sessionFactory;

    @Inject
    BundleContext bundleContext;

    @Configuration
    public static Option[] configuration() throws Exception {
        return new Option[]
                {
                        karafDistributionConfiguration()
                                .frameworkUrl(KARAF_URL)
                                .unpackDirectory(new File("target/paxexam/unpack")),
                        replaceConfigurationFile(BUNDLE_INSTALL_ACL_CFG,
                                new File("src/test/resources/etc/bundleinstall.cfg")),
                        mavenBundle()
                                .groupId(OSGIFX_GROUP_ID)
                                .artifactId(OSGIFX_BOOT_ARTIFACT_ID)
                                .versionAsInProject(),
                        logLevel(LogLevelOption.LogLevel.INFO),
                        keepRuntimeFolder()
                };
    }

    @Before
    public void setUp()
    {
        session = sessionFactory.create(System.in, printStream, errStream);
    }

    @Test
    public void testInstallBundle() throws Exception {
        final String installCommand ="bundle:install mvn:"
                + OSGIFX_GROUP_ID + "/"
                + IT_DUMMY_BUNDLE_ARTIFACT_ID + "/"
                + PROJECT_VERSION;
        session.execute(installCommand);
        Bundle dummyBundle = null;
        for (Bundle bundle : bundleContext.getBundles())
        {
            if (bundle.getSymbolicName() != null && bundle.getSymbolicName().equals(OSGIFX_GROUP_ID + "." + IT_DUMMY_BUNDLE_ARTIFACT_ID))
            {
                dummyBundle = bundle;
                break;
            }
        }
        assertNotNull(dummyBundle);
    }

    @Test
    public void testOsgiFxInstalled()
    {
        Bundle osgiFxBundle = null;
        for (Bundle bundle : bundleContext.getBundles() )
        {
            if (bundle.getSymbolicName() != null && bundle.getSymbolicName().equals(OSGIFX_GROUP_ID + "." + OSGIFX_BOOT_ARTIFACT_ID))
            {
                osgiFxBundle = bundle;
                break;
            }
        }

        assertNotNull(osgiFxBundle);
        assertEquals(OSGIFX_GROUP_ID + "." + OSGIFX_BOOT_ARTIFACT_ID + " is not active.", Bundle.ACTIVE, osgiFxBundle.getState());
    }

}
