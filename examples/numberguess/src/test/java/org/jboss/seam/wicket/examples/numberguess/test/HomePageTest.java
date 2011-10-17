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
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Test class for HomePage and SeamWicketTester.
 *
 * @author oranheim
 * @author Jozef Hartinger
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
                .addAsLibraries(Dependencies.SOLDER, Dependencies.WICKET, Dependencies.SLF4J);
        return war;
    }

    @Inject
    SeamWicketTester tester;

    @Test
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
    public void testRestart() throws Exception {
        Assert.assertNotNull(tester);

        tester.startPage(HomePage.class);

        FormTester form = tester.newFormTester("NumberGuessMain");
        Assert.assertNotNull(form);

        form.submit("RestartButton");

        tester.assertRenderedPage(HomePage.class);
    }
}
