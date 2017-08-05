package com.javatechnics.osgifx.boot;

import com.javatechnics.osgifx.OsgiFxTestConstants;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.karaf.options.LogLevelOption;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

import javax.inject.Inject;

import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.karafDistributionConfiguration;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.logLevel;

@ExamReactorStrategy(PerClass.class)
@RunWith(PaxExam.class)
public class DeployOsgiFxTest {

    @Inject
    protected BundleContext bundleContext;

    @Inject
    private LogService logService;

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
    public void testBundleDeployed() {
        TestCase.assertNotNull(bundleContext);
        TestCase.assertNotNull(logService);


    }
}
