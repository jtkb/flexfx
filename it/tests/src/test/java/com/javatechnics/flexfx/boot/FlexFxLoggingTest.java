package com.javatechnics.flexfx.boot;

import javafx.stage.Stage;
import org.apache.karaf.features.BootFinished;
import org.apache.karaf.log.core.LogService;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.api.console.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.ops4j.pax.logging.spi.PaxLoggingEvent;
import org.osgi.framework.BundleContext;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.TimeoutException;

import static com.javatechnics.flexfx.FlexFxTestConstants.BUNDLE_INSTALL_ACL_CFG;
import static com.javatechnics.flexfx.FlexFxTestConstants.DUMMY_DS_BUNDLE_ARTIFACT_ID;
import static com.javatechnics.flexfx.FlexFxTestConstants.FLEXFX_BOOT_ARTIFACT_ID;
import static com.javatechnics.flexfx.FlexFxTestConstants.FLEXFX_GROUP_ID;
import static com.javatechnics.flexfx.FlexFxTestConstants.KARAF_URL;
import static com.javatechnics.flexfx.FlexFxTestConstants.TESTFX_CORE_ARTIFACT_ID;
import static com.javatechnics.flexfx.FlexFxTestConstants.TESTFX_GROUP_ID;
import static com.javatechnics.flexfx.FlexFxTestConstants.TESTFX_INTERNAL_ARTIFACT_ID;
import static com.javatechnics.flexfx.FlexFxTestConstants.TESTFX_JUNIT_ARTIFACT_ID;
import static com.javatechnics.flexfx.FlexFxTestConstants.TESTFX_VERSION;
import static com.javatechnics.flexfx.boot.JavaFxExceptionMessages.JAVAFX_UNCAUGHT_EXCEPTION;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.systemPackage;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.features;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.keepRuntimeFolder;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.replaceConfigurationFile;

@ExamReactorStrategy(PerClass.class)
@RunWith(PaxExam.class)
public class FlexFxLoggingTest
{
    protected ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    protected PrintStream printStream = new PrintStream(byteArrayOutputStream);
    protected PrintStream errStream = new PrintStream(byteArrayOutputStream);
    protected Session session;

    @Inject
    protected BootFinished bootFinished;

    @Inject
    protected SessionFactory sessionFactory;

    @Inject
    protected LogService logService;

    @Inject
    BundleContext bundleContext;

    protected Stage primaryStage;

    @Before
    public void setUp() throws TimeoutException
    {
        session = sessionFactory.create(System.in, printStream, errStream);
        primaryStage = FxToolkit.registerStage(() -> new Stage());
    }

    @Configuration
    public Option[] configuration() throws Exception
    {
        return new Option[]
                {
                        karafDistributionConfiguration()
                                .frameworkUrl(KARAF_URL)
                                .unpackDirectory(new File("target/paxexam/unpack")),
                        replaceConfigurationFile(BUNDLE_INSTALL_ACL_CFG,
                                new File("src/test/resources/etc/bundleinstall.cfg")),
                        systemPackage("com.sun.glass.ui"),
                        mavenBundle()
                                .groupId(FLEXFX_GROUP_ID)
                                .artifactId(FLEXFX_BOOT_ARTIFACT_ID)
                                .versionAsInProject(),
                        mavenBundle().groupId(FLEXFX_GROUP_ID).artifactId(DUMMY_DS_BUNDLE_ARTIFACT_ID).versionAsInProject(),
                        mavenBundle(TESTFX_GROUP_ID, TESTFX_CORE_ARTIFACT_ID, TESTFX_VERSION),
                        mavenBundle(TESTFX_GROUP_ID, TESTFX_INTERNAL_ARTIFACT_ID, TESTFX_VERSION),
                        mavenBundle(TESTFX_GROUP_ID, TESTFX_JUNIT_ARTIFACT_ID, TESTFX_VERSION),
                        logLevel(LogLevelOption.LogLevel.ERROR),
                        features(mavenBundle().groupId("org.apache.karaf.features")
                                .artifactId("standard").type("xml").classifier("features")
                                .versionAsInProject(), "scr"),
                        keepRuntimeFolder()
                };
    }

    @Test
    public void testUnhandledExceptionLogged()
    {
        final FxRobot fxRobot = new FxRobot();

        // Check no exceptions logged.
        final Iterable<PaxLoggingEvent> preTestLoggingEvents = logService.getEvents();
        for (final PaxLoggingEvent event : preTestLoggingEvents)
        {
            Assert.assertNotEquals(JAVAFX_UNCAUGHT_EXCEPTION, event.getMessage());
        }

        // Click the button which should result in a NullPointerException being logged.
        fxRobot.clickOn("#buttonOne");

        // Check the logs for the uncaught exception.
        final Iterable<PaxLoggingEvent> events = logService.getEvents();
        String uncaughtExceptionMessage = "";
        for (final PaxLoggingEvent event : events)
        {
            uncaughtExceptionMessage = event.getMessage();
            if (JAVAFX_UNCAUGHT_EXCEPTION.equals(uncaughtExceptionMessage))
            {
                break;
            }
        }

        Assert.assertEquals(JAVAFX_UNCAUGHT_EXCEPTION, uncaughtExceptionMessage);
    }
}
