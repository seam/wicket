package org.jboss.seam.wicket.mock;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

@Singleton
public class SeamWicketTesterProducer
{
   @Produces
   public SeamWicketTester produceWicketTester()
   {
      return new SeamWicketTester();
   }

}
