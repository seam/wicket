package org.jboss.seam.wicket.example.numberguess.test;

import javax.inject.Inject;

import org.apache.wicket.util.tester.FormTester;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.wicket.SeamApplication;
import org.jboss.seam.wicket.example.numberguess.HomePage;
import org.jboss.seam.wicket.example.numberguess.NumberGuessApplication;
import org.jboss.seam.wicket.example.numberguess.test.util.MavenArtifactResolver;
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
 */
@RunWith(Arquillian.class)
public class HomePageTest
{

   @Deployment
   public static WebArchive createTestArchive()
   {
      return ShrinkWrap.create(WebArchive.class, "test.war")
         .addPackage(SeamApplication.class.getPackage())
         .addPackage(NonContextual.class.getPackage())
         .addPackage(NumberGuessApplication.class.getPackage())
         .addPackage(SeamWicketTester.class.getPackage())
         // ugh, arquillian, don't make this so painful :(
         .addResource("org/jboss/seam/wicket/example/numberguess/HomePage.html", "WEB-INF/classes/org/jboss/seam/wicket/example/numberguess/HomePage.html")
         .addWebResource("test-jetty-env.xml", "jetty-env.xml")
         .addWebResource(EmptyAsset.INSTANCE, "beans.xml")
         .addLibraries(
               MavenArtifactResolver.resolve("org.jboss.seam.solder:seam-solder:3.0.0.Beta1"),
               MavenArtifactResolver.resolve("org.apache.wicket:wicket:1.4.14"))
         .setWebXML("test-web.xml");
   }
   
   @Inject
   SeamWicketTester tester;
   
   @Test
   public void testGuessNumber() throws Exception
   {
      Assert.assertNotNull(tester);
      
      tester.startPage(HomePage.class);
      
      FormTester form = tester.newFormTester("NumberGuessMain");
      Assert.assertNotNull(form);
      
      form.setValue("inputGuess", "1");
      form.submit("GuessButton");
      
      tester.assertRenderedPage(HomePage.class);
   }
   
   @Test
   public void testRestart() throws Exception
   {
      Assert.assertNotNull(tester);
      
      tester.startPage(HomePage.class);
      
      FormTester form = tester.newFormTester("NumberGuessMain");
      Assert.assertNotNull(form);
      
      form.submit("RestartButton");
      
      tester.assertRenderedPage(HomePage.class);
   }
}
