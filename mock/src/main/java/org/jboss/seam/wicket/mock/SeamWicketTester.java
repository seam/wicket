package org.jboss.seam.wicket.mock;

import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.SecondLevelCacheSessionStore;
import org.apache.wicket.protocol.http.SecondLevelCacheSessionStore.IPageStore;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.util.tester.DummyHomePage;
import org.apache.wicket.util.tester.WicketTester;
import org.jboss.seam.wicket.SeamApplication;

/**
 * A helper class for testing Seam Pages containing CDI beans. The
 * SeamWicketTester inherits CDI lifecycle management provided by
 * SeamApplication. It's a minor rewrite of Wicket's WicketTester (Wicket
 * documentation states since version 1.2.6, where static classes orignates from
 * version 1.4.14)
 * 
 * @author oranheim
 * 
 * @see WicketTester
 * @see SeamApplication
 */
public class SeamWicketTester extends WicketTester
{

   /**
    * Default dummy seam web application for testing. Uses
    * {@link HttpSessionStore} to store pages and the <code>Session</code>.
    */
   public static class DummySeamApplication extends SeamApplication
   {
      /**
       * @see org.apache.wicket.Application#getHomePage()
       */
      @Override
      public Class<? extends Page> getHomePage()
      {
         return DummyHomePage.class;
      }

      @Override
      protected ISessionStore newSessionStore()
      {
         // Don't use a filestore, or we spawn lots of threads, which makes
         // things slow.
         return new HttpSessionStore(this);
      }

      /**
       * @see org.apache.wicket.protocol.http.WebApplication#newWebResponse(javax.servlet.http.HttpServletResponse)
       */
      @Override
      protected WebResponse newWebResponse(final HttpServletResponse servletResponse)
      {
         return new WebResponse(servletResponse);
      }

      @Override
      protected void outputDevelopmentModeWarning()
      {
         // do nothing
      }
   }

   /**
    * Dummy web application that does not support back button support but is
    * cheaper to use for unit tests. Uses {@link SecondLevelCacheSessionStore}
    * with a noop {@link IPageStore}.
    */
   public static class NonPageCachingDummySeamApplication extends DummySeamApplication
   {
      @Override
      protected ISessionStore newSessionStore()
      {
         return new SecondLevelCacheSessionStore(this, new IPageStore()
         {
            public void destroy()
            {
            }

            public Page getPage(String sessionId, String pagemap, int id, int versionNumber, int ajaxVersionNumber)
            {
               return null;
            }

            public void pageAccessed(String sessionId, Page page)
            {
            }

            public void removePage(String sessionId, String pagemap, int id)
            {
            }

            public void storePage(String sessionId, Page page)
            {
            }

            public void unbind(String sessionId)
            {
            }

            public boolean containsPage(String sessionId, String pageMapName, int pageId, int pageVersion)
            {
               return false;
            }
         });
      }
   }

   public SeamWicketTester()
   {
      super(new DummySeamApplication());
   }

   /**
    * Creates a <code>SeamWicketTester</code> and automatically creates a
    * <code>SeamApplication</code>.
    * 
    * @param homePage a home page <code>Class</code>
    */
   public SeamWicketTester(final Class<? extends Page> homePage)
   {
      super(new SeamApplication()
      {
         /**
          * @see org.apache.wicket.Application#getHomePage()
          */
         @Override
         public Class<? extends Page> getHomePage()
         {
            return homePage;
         }

         @Override
         protected ISessionStore newSessionStore()
         {
            // Don't use a filestore, or we spawn lots of threads, which
            // makes things slow.
            return new HttpSessionStore(this);
         }

         @Override
         protected WebResponse newWebResponse(final HttpServletResponse servletResponse)
         {
            return new WebResponse(servletResponse);
         }

         @Override
         protected void outputDevelopmentModeWarning()
         {
            // Do nothing.
         }
      });
   }

}
