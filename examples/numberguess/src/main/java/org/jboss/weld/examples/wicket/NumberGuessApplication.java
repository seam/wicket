package org.jboss.weld.examples.wicket;

import org.jboss.seam.wicket.SeamApplication;

public class NumberGuessApplication extends SeamApplication
{
   @Override
   public Class getHomePage()
   {
      return HomePage.class;
   }
}
