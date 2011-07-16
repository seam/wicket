package org.jboss.seam.wicket;

import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.jboss.seam.wicket.util.NonContextual;

/**
 * A subclass of InjectingSeamApplication which adds management of
 * JSR-299 conversation scopes with Wicket page metadata. 
 * 
 * If you have your own
 * WebApplication subclass, and can't subclass this class, you just need to do
 * the three things that this class does, i.e. register the
 * SeamComponentInstantiationListener, and override the two methods below to
 * return the RequestCycle and IRequestCycleProcessor subclasses specific to
 * Seam, or your subclasses of those classes.
 *
 * @author cpopetz
 * @author pmuir
 * @author ivaynberg
 * @see WebApplication
 * @see SeamWebRequestCycleProcessor
 * @see SeamRequestCycle
 */
public abstract class SeamApplication extends InjectingSeamApplication {

    private NonContextual<SeamComponentInstantiationListener> seamComponentInstantiationListener;
    private NonContextual<SeamWebRequestCycleProcessor> seamWebRequestCycleProcessor;

    
    /**
     * @returns SeamWebRequestCycleProcessor
     */
    @Override
    protected Class<? extends SeamWebRequestCycleProcessor>
    getWebRequestCycleProcessorClass() {
        return SeamWebRequestCycleProcessor.class;
    }

    /**
     * Override to return our Seam-specific request cycle
     *
     * @see SeamRequestCycle
     */
    @Override
    public RequestCycle newRequestCycle(final Request request, final Response response) {
        return new SeamRequestCycle(this, (WebRequest) request, (WebResponse) response);
    }
}
