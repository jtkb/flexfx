package com.javatechnics.osgifx.boot;

import com.javatechnics.osgifx.OsgiFxTestConstants;
import com.javatechnics.osgifx.stage.StageService;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

@ExamReactorStrategy(PerClass.class)
@RunWith(PaxExam.class)
public class DeployOsgiFxTest {

    @Inject
    protected StageService stageService;

    @Configuration
    public static Option[] configuration() throws Exception {
        return new Option[]
                {
                        karafDistributionConfiguration()
                                .frameworkUrl(OsgiFxTestConstants.KARAF_URL),
                        mavenBundle()
                                .groupId(OsgiFxTestConstants.OSGIFX_GROUP_ID)
                                .artifactId(OsgiFxTestConstants.OSGIFX_BOOT_ARTIFACT_ID)
                                .versionAsInProject(),
                        logLevel(LogLevelOption.LogLevel.INFO)
                };
    }

    @Test
    public void testStageServiceAvailable()
    {
        assertNotNull("Stage service is NULL", stageService);
    }

    @Test
    public void testObtainingStage()
    {
        assertNotNull("Stage service is NULL", stageService);
        final Stage stage = stageService.getStage();
        assertNotNull("Stage object is NULL", stage);
    }

}
