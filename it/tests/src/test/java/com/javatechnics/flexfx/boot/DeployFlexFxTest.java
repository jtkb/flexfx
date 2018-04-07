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

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import static com.javatechnics.flexfx.FlexFxTestConstants.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.*;

@ExamReactorStrategy(PerClass.class)
@RunWith(PaxExam.class)
public class DeployFlexFxTest
{
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
    public static Option[] configuration() throws Exception
    {
        return new Option[]
                {
                        karafDistributionConfiguration()
                                .frameworkUrl(KARAF_URL)
                                .unpackDirectory(new File(PAX_EXAM_UNPACK_DIR)),
                        replaceConfigurationFile(BUNDLE_INSTALL_ACL_CFG,
                                new File(KARAF_CFG_FILE)),
                        mavenBundle()
                                .groupId(FLEXFX_GROUP_ID)
                                .artifactId(FLEXFX_BOOT_ARTIFACT_ID)
                                .versionAsInProject(),
                        logLevel(LogLevelOption.LogLevel.INFO)
                };
    }

    @Before
    public void setUp()
    {
        session = sessionFactory.create(System.in, printStream, errStream);
    }

    @Test
    public void testInstallBundle() throws Exception
    {
        final String installCommand = "bundle:install mvn:"
                + FLEXFX_GROUP_ID + "/"
                + IT_DUMMY_BUNDLE_ARTIFACT_ID + "/"
                + FLEXFX_VERSION;
        session.execute(installCommand);
        Bundle dummyBundle = null;
        for (Bundle bundle : bundleContext.getBundles())
        {
            if (bundle.getSymbolicName() != null && bundle.getSymbolicName().equals(FLEXFX_GROUP_ID + "." + IT_DUMMY_BUNDLE_ARTIFACT_ID))
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
        for (Bundle bundle : bundleContext.getBundles())
        {
            if (bundle.getSymbolicName() != null && bundle.getSymbolicName().equals(FLEXFX_GROUP_ID + "." + FLEXFX_BOOT_ARTIFACT_ID))
            {
                osgiFxBundle = bundle;
                break;
            }
        }

        assertNotNull(osgiFxBundle);
        assertEquals(FLEXFX_GROUP_ID + "." + FLEXFX_BOOT_ARTIFACT_ID + " is not active.", Bundle.ACTIVE, osgiFxBundle.getState());
    }

}
