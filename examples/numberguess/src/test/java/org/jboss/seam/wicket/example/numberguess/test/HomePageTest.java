package org.jboss.seam.wicket.example.numberguess.test;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.TagTester;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.wicket.example.numberguess.HomePage;
import org.jboss.seam.wicket.mock.SeamWicketTester;
import org.jboss.shrinkwrap.api.ArchivePaths;
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
         .addClass(SeamWicketTester.class)
         .addClasses(HomePage.class)
         .addWebResource("test-jetty-env.xml", "jetty-env.xml")
         .addWebResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
         .addManifestResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
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
