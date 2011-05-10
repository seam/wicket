package org.jboss.seam.wicket.mock;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.HttpSessionStore;
import org.apache.wicket.protocol.http.SecondLevelCacheSessionStore;
import org.apache.wicket.protocol.http.SecondLevelCacheSessionStore.IPageStore;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.session.ISessionStore;
import org.apache.wicket.util.tester.DummyHomePage;
import org.apache.wicket.util.tester.WicketTester;
import org.jboss.seam.wicket.SeamApplication;
import org.jboss.seam.wicket.SeamRequestCycle;
import org.jboss.weld.context.http.HttpRequestContext;

/**
 * A helper class for testing Seam Pages containing CDI beans. The
 * SeamWicketTester inherits CDI lifecycle management provided by
 * SeamApplication. It's a minor rewrite of Wicket's WicketTester (Wicket
 * documentation states since version 1.2.6, where static classes orignates from
 * version 1.4.14)
 *
 * @author oranheim
 * @see WicketTester
 * @see SeamApplication
 */
public class SeamWicketTester extends WicketTester {

    @Inject
    HttpRequestContext requestContext;

    /*
    * Override because there is need for manualy restart request context in
    * wicket tester.
    *
    * @see
    * org.apache.wicket.protocol.http.MockWebApplication#setupRequestAndResponse
    * (boolean)
    */
    @Override
    public WebRequestCycle setupRequestAndResponse(boolean isAjax) {
        if (requestContext.isActive()) {
            endRequest(getServletRequest());
        }
        WebRequestCycle webRequestCycle = super.setupRequestAndResponse(isAjax);
        startRequest(getServletRequest());
        return webRequestCycle;
    }

    /*
    * Start the request, providing a data store which will last the lifetime of
    * the request
    */

    public void startRequest(ServletRequest servletRequest) {
        // Associate the store with the context and acticate the context
        requestContext.associate(servletRequest);
        requestContext.activate();
    }

    /*
    * End the request, providing the same data store as was used to start the
    * request
    */
    public void endRequest(ServletRequest servletRequest) {
        try {
            /*
            * Invalidate the request (all bean instances will be scheduled for
            * destruction)
            */
            requestContext.invalidate();
            /*
            * Deactivate the request, causing all bean instances to be destroyed
            * (as the context is invalid)
            */
            requestContext.deactivate();
        } finally {

            /*
            * Ensure that whatever happens we dissociate to prevent any memory
            * leaks
            */
            requestContext.dissociate(servletRequest);
        }
    }

    /**
     * Default dummy seam web application for testing. Uses
     * {@link HttpSessionStore} to store pages and the <code>Session</code>.
     */
    public static class DummySeamApplication extends SeamApplication {
        // For manually detaching wicket request object and conversation scope
        private SeamTestRequestCycle seamTestRequestCycle;

        private boolean manuallyDetach = false;

        /**
         * @see org.apache.wicket.Application#getHomePage()
         */
        @Override
        public Class<? extends Page> getHomePage() {
            return DummyHomePage.class;
        }

        @Override
        protected ISessionStore newSessionStore() {
            // Don't use a filestore, or we spawn lots of threads, which makes
            // things slow.
            return new HttpSessionStore(this);
        }

        /**
         * @see org.apache.wicket.protocol.http.WebApplication#newWebResponse(javax.servlet.http.HttpServletResponse)
         */
        @Override
        protected WebResponse newWebResponse(final HttpServletResponse servletResponse) {
            return new WebResponse(servletResponse);
        }

        @Override
        protected void outputDevelopmentModeWarning() {
            // do nothing
        }

        /**
         * Override to return Seam-specific request cycle with manually detach
         * control.
         *
         * @see SeamRequestCycle
         */
        @Override
        public RequestCycle newRequestCycle(final Request request, final Response response) {
            seamTestRequestCycle = new SeamTestRequestCycle(this, (WebRequest) request, (WebResponse) response, !manuallyDetach);
            return seamTestRequestCycle;
        }

        /**
         * Detach wicket request object and conversation scope. Should be call
         * after all tests on current page.
         */
        public void detach() {
            if (seamTestRequestCycle != null) {
                seamTestRequestCycle.superDetach();
            }
        }

        /**
         * Whether wicket request and conversation scope objects should be
         * manually detach or will be detached immediate after rendering page.
         *
         * @return
         */
        public boolean isManuallyDetach() {
            return !seamTestRequestCycle.isDetach();
        }

        /**
         * @param manuallyDetach wicket request and conversation scope objects
         *                       instead of immediate after rendering page.
         */
        public void setManuallyDetach(boolean manuallyDetach) {
            this.manuallyDetach = manuallyDetach;
            this.seamTestRequestCycle.setDetach(!manuallyDetach);
        }
    }

    /**
     * Dummy web application that does not support back button support but is
     * cheaper to use for unit tests. Uses {@link SecondLevelCacheSessionStore}
     * with a noop {@link IPageStore}.
     */
    public static class NonPageCachingDummySeamApplication extends DummySeamApplication {
        @Override
        protected ISessionStore newSessionStore() {
            return new SecondLevelCacheSessionStore(this, new IPageStore() {
                public void destroy() {
                }

                public Page getPage(String sessionId, String pagemap, int id, int versionNumber, int ajaxVersionNumber) {
                    return null;
                }

                public void pageAccessed(String sessionId, Page page) {
                }

                public void removePage(String sessionId, String pagemap, int id) {
                }

                public void storePage(String sessionId, Page page) {
                }

                public void unbind(String sessionId) {
                }

                public boolean containsPage(String sessionId, String pageMapName, int pageId, int pageVersion) {
                    return false;
                }
            });
        }
    }

    public SeamWicketTester() {
        super(new DummySeamApplication());
    }

    /**
     * Creates a <code>SeamWicketTester</code> and automatically creates a
     * <code>SeamApplication</code>.
     *
     * @param homePage a home page <code>Class</code>
     */
    public SeamWicketTester(final Class<? extends Page> homePage) {
        super(new SeamApplication() {
            /**
             * @see org.apache.wicket.Application#getHomePage()
             */
            @Override
            public Class<? extends Page> getHomePage() {
                return homePage;
            }

            @Override
            protected ISessionStore newSessionStore() {
                // Don't use a filestore, or we spawn lots of threads, which
                // makes things slow.
                return new HttpSessionStore(this);
            }

            @Override
            protected WebResponse newWebResponse(final HttpServletResponse servletResponse) {
                return new WebResponse(servletResponse);
            }

            @Override
            protected void outputDevelopmentModeWarning() {
                // Do nothing.
            }
        });
    }

}
