package org.jboss.seam.wicket.example.numberguess;

import org.jboss.seam.wicket.SeamApplication;

public class NumberGuessApplication extends SeamApplication
{
   @Override
   public Class<HomePage> getHomePage()
   {
      return HomePage.class;
   }
}
