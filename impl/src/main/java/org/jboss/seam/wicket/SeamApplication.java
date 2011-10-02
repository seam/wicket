package org.jboss.seam.wicket;

import javax.enterprise.inject.spi.BeanManager;

import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.IRequestCycleProcessor;
import org.jboss.solder.beanManager.BeanManagerLocator;
import org.jboss.seam.wicket.util.NonContextual;

/**
 * A convenience subclass of wicket's WebApplication which adds the hooks
 * necessary to use JSR-299 injections in wicket components, as well as manage
 * JSR-299 conversation scopes with Wicket page metadata. If you have your own
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
public abstract class SeamApplication extends WebApplication {

    private NonContextual<SeamComponentInstantiationListener> seamComponentInstantiationListener;
    private NonContextual<SeamWebRequestCycleProcessor> seamWebRequestCycleProcessor;

    /**
     */
    public SeamApplication() {
    }

    /**
     * Add our component instantiation listener
     *
     * @see SeamComponentInstantiationListener
     */
    @Override
    protected void internalInit() {
        super.internalInit();
        BeanManager manager = new BeanManagerLocator().getBeanManager();
        this.seamComponentInstantiationListener = NonContextual.of(SeamComponentInstantiationListener.class, manager);
        this.seamWebRequestCycleProcessor = NonContextual.of(getWebRequestCycleProcessorClass(), manager);
        addComponentInstantiationListener(seamComponentInstantiationListener.newInstance().produce().inject().get());
    }

    protected Class<? extends SeamWebRequestCycleProcessor>
    getWebRequestCycleProcessorClass() {
        return SeamWebRequestCycleProcessor.class;
    }

    /**
     * Override to return our Seam-specific request cycle processor
     *
     * @see SeamWebRequestCycleProcessor
     */
    @Override
    protected IRequestCycleProcessor newRequestCycleProcessor() {
        return seamWebRequestCycleProcessor.newInstance().produce().inject().get();
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
