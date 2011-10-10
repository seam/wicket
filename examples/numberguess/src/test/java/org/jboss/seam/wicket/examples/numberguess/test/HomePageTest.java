package org.jboss.seam.wicket.examples.numberguess.test;

import javax.inject.Inject;

import junit.framework.AssertionFailedError;

import org.apache.wicket.util.tester.FormTester;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.wicket.SeamApplication;
import org.jboss.seam.wicket.examples.numberguess.HomePage;
import org.jboss.seam.wicket.examples.numberguess.NumberGuessApplication;
import org.jboss.seam.wicket.mock.SeamWicketTester;
import org.jboss.seam.wicket.util.NonContextual;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test class for HomePage and SeamWicketTester.
 *
 * @author oranheim
 */
@RunWith(Arquillian.class)
public class HomePageTest{

    @Deployment
    public static WebArchive createTestArchive() {
       
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(SeamApplication.class.getPackage())
                .addPackage(NonContextual.class.getPackage())
                .addPackage(NumberGuessApplication.class.getPackage())
                .addPackage(SeamWicketTester.class.getPackage())
                .addClasses(Assert.class, AssertionFailedError.class)
                        // ugh, arquillian, don't make this so painful :(
                .addAsResource("org/jboss/seam/wicket/examples/numberguess/HomePage.html", "org/jboss/seam/wicket/examples/numberguess/HomePage.html")
                .addAsWebResource("test-jetty-env.xml", "jetty-env.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsWebInfResource(new StringAsset("<jboss-deployment-structure>\n" +
                        " <deployment>\n" +
                        " <dependencies>\n" +
                        " <module name=\"org.jboss.logmanager\" />\n" +
                        " </dependencies>\n" +
                        " </deployment>\n" +
                        "</jboss-deployment-structure>"), "jboss-deployment-structure.xml")
                .addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class).artifact("org.apache.wicket:wicket:1.4.15").resolveAs(JavaArchive.class))
                .addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class).artifact("org.jboss.seam.solder:seam-solder:3.1.0.Beta2").resolveAs(JavaArchive.class))
                .addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class).artifact("org.slf4j:slf4j-simple:1.6.1").resolveAs(JavaArchive.class));
        return war;
    }

    @Inject
    SeamWicketTester tester;

    @Test
    @Ignore // SEAMWICKET-44
    public void testGuessNumber() throws Exception {
        Assert.assertNotNull(tester);

        tester.startPage(HomePage.class);

        FormTester form = tester.newFormTester("NumberGuessMain");
        Assert.assertNotNull(form);

        form.setValue("inputGuess", "1");
        form.submit("GuessButton");

        tester.assertRenderedPage(HomePage.class);
    }

    @Test
    @Ignore // SEAMWICKET-44
    public void testRestart() throws Exception {
        Assert.assertNotNull(tester);

        tester.startPage(HomePage.class);

        FormTester form = tester.newFormTester("NumberGuessMain");
        Assert.assertNotNull(form);

        form.submit("RestartButton");

        tester.assertRenderedPage(HomePage.class);
    }
}
