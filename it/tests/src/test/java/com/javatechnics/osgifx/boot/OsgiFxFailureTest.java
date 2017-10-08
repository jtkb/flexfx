package com.javatechnics.osgifx.boot;

import javafx.stage.Stage;
import org.apache.karaf.features.BootFinished;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.api.console.SessionFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.testfx.api.FxToolkit;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.TimeoutException;

import static com.javatechnics.osgifx.OsgiFxTestConstants.*;
import static com.javatechnics.osgifx.OsgiFxTestConstants.TESTFX_ARTIFACT_ID;
import static com.javatechnics.osgifx.OsgiFxTestConstants.TESTFX_VERSION;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.replaceConfigurationFile;

@ExamReactorStrategy(PerClass.class)
@RunWith(PaxExam.class)
public class OsgiFxFailureTest
{
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private PrintStream printStream = new PrintStream(byteArrayOutputStream);
    private PrintStream errStream = new PrintStream(byteArrayOutputStream);
    private Session session;
    private Stage primaryStage;

    @Inject
    protected BootFinished bootFinished;

    @Inject
    private SessionFactory sessionFactory;

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
                        mavenBundle()
                                .groupId(OSGIFX_GROUP_ID)
                                .artifactId(OSGIFX_BOOT_ARTIFACT_ID)
                                .versionAsInProject(),
                        mavenBundle()
                                .groupId(OSGIFX_GROUP_ID)
                                .artifactId(IT_DUMMY_BUNDLE_ARTIFACT_ID)
                                .versionAsInProject(),
                        mavenBundle(GUAVA_GROUP_ID, GUAVA_ARTIFACT_ID, GUAVA_VERSION),
                        mavenBundle(TESTFX_GROUP_ID, TESTFX_ARTIFACT_ID, TESTFX_VERSION),
                        logLevel(LogLevelOption.LogLevel.INFO),
                };
    }

    @Before
    public void setUp() throws TimeoutException
    {
        session = sessionFactory.create(System.in, printStream, errStream);
        primaryStage  = FxToolkit.registerStage(Stage::new);
    }
}
