package org.jboss.seam.wicket;

import javax.enterprise.context.Conversation;
import javax.inject.Inject;

import org.apache.wicket.Page;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.protocol.http.WebRequestCycleProcessor;

/**
 * SeamWebRequestCycleProcessor is a subclass of the standard wicket
 * WebRequestCycleProcessor which saves the conversation id of any long-running
 * conversation in wicket page metadata.
 *
 * @author cpopetz
 * @author ivaynberg
 */
public class SeamWebRequestCycleProcessor extends WebRequestCycleProcessor {
    @Inject
    Conversation conversation;

    /**
     * If a long running conversation has been started, store its id into page
     * metadata
     */
    @Override
    public void respond(RequestCycle requestCycle) {
        super.respond(requestCycle);
        if (!conversation.isTransient()) {
            Page page = RequestCycle.get().getResponsePage();
            if (page != null) {
                page.setMetaData(SeamMetaData.CID, conversation.getId());
            }
        }

    }
}
