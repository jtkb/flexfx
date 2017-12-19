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

package com.javatechnics.flexfx.boot;

import javafx.stage.Stage;
import org.apache.karaf.features.BootFinished;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.api.console.SessionFactory;
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
import org.testfx.api.FxToolkit;
import org.testfx.osgi.service.TestFx;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.TimeoutException;

import static com.javatechnics.flexfx.OsgiFxTestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.base.NodeMatchers.hasText;

@ExamReactorStrategy(PerClass.class)
@RunWith(PaxExam.class)
public class TFxTest
{

    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private PrintStream printStream = new PrintStream(byteArrayOutputStream);
    private PrintStream errStream = new PrintStream(byteArrayOutputStream);
    private Session session;
    private Stage primaryStage;

    @Inject
    protected BootFinished bootFinished;

    @Inject
    protected TestFx testFx;

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
                        replaceConfigurationFile(CONFIG_PROPERTIES,
                                new File("src/test/resources/etc/config.properties")),
                        mavenBundle()
                                .groupId(OSGIFX_GROUP_ID)
                                .artifactId(OSGIFX_BOOT_ARTIFACT_ID)
                                .versionAsInProject(),
                        mavenBundle()
                                .groupId(OSGIFX_GROUP_ID)
                                .artifactId(IT_DUMMY_BUNDLE_ARTIFACT_ID)
                                .versionAsInProject(),
                        mavenBundle(TESTFX_GROUP_ID, TESTFX_CORE_ARTIFACT_ID, TESTFX_VERSION),
                        mavenBundle(TESTFX_GROUP_ID, TESTFX_INTERNAL_ARTIFACT_ID, TESTFX_VERSION),
                        logLevel(LogLevelOption.LogLevel.INFO)
                };
    }

    @Before
    public void setUp() throws TimeoutException {
        session = sessionFactory.create(System.in, printStream, errStream);
        primaryStage  = FxToolkit.registerStage(() -> new Stage());
    }

    @Test
    public void testOsgiFxInstalled()
    {
        final Bundle osgiFxBundle = getInstalledBundle(OSGIFX_GROUP_ID + "." + OSGIFX_BOOT_ARTIFACT_ID);
        assertNotNull(osgiFxBundle);
        assertEquals(OSGIFX_GROUP_ID + "." + OSGIFX_BOOT_ARTIFACT_ID + " is not active.", Bundle.ACTIVE, osgiFxBundle.getState());
    }

    @Test
    public void testStageAvailable()
    {
        if (primaryStage == null)
        {
            System.out.print("Primary stage is null");
        }
        else
        {
            System.out.println("Primary stage is not null");
        }
    }

    @Test
    public void testButtonAvailable()
    {
        verifyThat("#button", hasText("Button"));
    }

    /**
     * Ensures that if a second SceneService is deployed, that is it not loaded into the Stage.
     */
    @Test
    public void testSecondSceneServiceNotSelected() throws Exception
    {
        final String installDummyBundleTwoCommand ="bundle:install mvn:"
                + OSGIFX_GROUP_ID + "/"
                + DUMMY_TWO_BUNDLE_ARTIFACT_ID + "/"
                + PROJECT_VERSION;
        // Ensure the dummy bundle two is not installed.
        Bundle dummyTwoBundle = getInstalledBundle(OSGIFX_GROUP_ID + "." + DUMMY_TWO_BUNDLE_ARTIFACT_ID);
        assertNull("Dummy Bundle Two found to be already deployed.", dummyTwoBundle);

        session.execute(installDummyBundleTwoCommand);

        dummyTwoBundle = getInstalledBundle(OSGIFX_GROUP_ID + "." + DUMMY_TWO_BUNDLE_ARTIFACT_ID);
        assertNotNull("Dummy Bundle Two was not installed.", dummyTwoBundle);
        if (dummyTwoBundle.getState() != Bundle.ACTIVE)
        {
            dummyTwoBundle.start();
        }
        assertEquals("Dummy Two bundle not active.", Bundle.ACTIVE, dummyTwoBundle.getState());
        dummyTwoBundle.uninstall();

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
