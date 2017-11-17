package com.javatechnics.osgifx.boot;

import org.apache.karaf.features.BootFinished;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.api.console.SessionFactory;
import org.apache.karaf.shell.support.MultiException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import java.util.concurrent.TimeoutException;

import static com.javatechnics.osgifx.OsgiFxTestConstants.*;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*;

@ExamReactorStrategy(PerClass.class)
@RunWith(PaxExam.class)
public class OsgiFxFailureTest
{
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private PrintStream printStream = new PrintStream(byteArrayOutputStream);
    private PrintStream errStream = new PrintStream(byteArrayOutputStream);
    private Session session;
    private static final String installCommand = "bundle:install mvn:"
            + OSGIFX_GROUP_ID + "/"
            + OSGIFX_BOOT_ARTIFACT_ID + "/"
            + PROJECT_VERSION;

    @Inject
    protected BootFinished bootFinished;

    @Inject
    private SessionFactory sessionFactory;

    @Inject
    private BundleContext bundleContext;

    @Configuration
    public static Option[] configuration() throws Exception
    {
        return new Option[]
                {
                        karafDistributionConfiguration()
                                .frameworkUrl(KARAF_URL)
                                .unpackDirectory(new File("target/paxexam/unpack")),
                        replaceConfigurationFile(BUNDLE_INSTALL_ACL_CFG,
                                new File("src/test/resources/etc/bundleinstall.cfg")),
                        replaceConfigurationFile(CONFIG_PROPERTIES,
                                new File("src/test/resources/etc/config.properties")),
                        mavenBundle(TESTFX_GROUP_ID, TESTFX_CORE_ARTIFACT_ID, TESTFX_VERSION),
                        mavenBundle(TESTFX_GROUP_ID, TESTFX_INTERNAL_ARTIFACT_ID, TESTFX_VERSION),
                        logLevel(LogLevelOption.LogLevel.INFO)
                };
    }

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws TimeoutException
    {
        session = sessionFactory.create(System.in, printStream, errStream);
    }

    @Test
    public void testReinstallJavaFxFail() throws Exception
    {
        // Check OsgiFx bundle is not installed
        Bundle bootBundle = getInstalledBundle(OSGIFX_GROUP_ID + "." + OSGIFX_BOOT_ARTIFACT_ID);
        Assert.assertNull(bootBundle);

        //Install OsgiFx bundle
        session.execute(installCommand);

        //Check installed
        bootBundle = getInstalledBundle(OSGIFX_GROUP_ID + "." + OSGIFX_BOOT_ARTIFACT_ID);
        Assert.assertNotNull(bootBundle);
        long bundleId = bootBundle.getBundleId();
        final long firstBootBundleId = bundleId;
        // Check the bundle is in the installed state.
        Assert.assertEquals("Bundle expected to be in the installed state.", bootBundle.getState(), Bundle.INSTALLED);

        // Start the bundle
        session.execute("start " + bundleId);
        Assert.assertEquals("Bundle not started.", bootBundle.getState(), Bundle.ACTIVE);

        // Uninstall the bundle
        session.execute("uninstall " + bundleId);
        bootBundle = getInstalledBundle(OSGIFX_GROUP_ID + "." + OSGIFX_BOOT_ARTIFACT_ID);
        Assert.assertNull(bootBundle);

        // Re-install the bundle
        session.execute(installCommand);
        bootBundle = getInstalledBundle(OSGIFX_GROUP_ID + "." + OSGIFX_BOOT_ARTIFACT_ID);
        Assert.assertNotNull(bootBundle);
        bundleId = bootBundle.getBundleId();
        Assert.assertNotEquals(firstBootBundleId, bundleId);

        // Try starting the re-installed bundle.
        try
        {
            expectedException.expect(MultiException.class);
            session.execute("start " + bundleId);
        }
        finally
        {
            bootBundle = getInstalledBundle(OSGIFX_GROUP_ID + "." + OSGIFX_BOOT_ARTIFACT_ID);
            Assert.assertNotEquals("Boot bundle NOT expected to be ACTIVE" + bootBundle.getState(), Bundle.ACTIVE, bootBundle.getState());
        }
    }

    private Bundle getInstalledBundle(final String bundleSymbolicName)
    {
        Bundle installedBundle = null;
        for (Bundle bundle : bundleContext.getBundles())
        {
            if (bundle.getSymbolicName() != null && bundle.getSymbolicName().equals(bundleSymbolicName))
            {
                installedBundle = bundle;
                break;
            }
        }
        return installedBundle;
    }
}
