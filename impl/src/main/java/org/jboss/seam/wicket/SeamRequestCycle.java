package org.jboss.seam.wicket;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.Instance;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.Page;
import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.request.target.component.BookmarkablePageRequestTarget;
import org.apache.wicket.request.target.component.IPageRequestTarget;
import org.jboss.weld.Container;
import org.jboss.weld.context.ConversationContext;
import org.jboss.weld.context.http.HttpConversationContext;

/**
 * SeamRequestCycle is a subclass of the standard wicket WebRequestCycle which:
 * <ul>
 * <li>restores long-running conversations specified in wicket page metadata
 * when a page target is first used.
 * <li>propagates long running conversations to new page targets by specifying
 * the above metadata
 * <li>propagates long running conversations across redirects through the use of
 * a request parameter if the redirect is handled with a BookmarkablePageRequest
 * <li>Sets up the conversational context when the request target is set
 * <li>Tears down the conversation context on detach() of the RequestCycle
 * </ul>
 *
 * @author cpopetz
 * @see SeamWebRequestCycleProcessor Which handles propogation of conversation
 *      data for newly-started long running conversations, by storing their ids
 *      in the page metadata
 */
public class SeamRequestCycle extends WebRequestCycle {
    public SeamRequestCycle(WebApplication application, WebRequest request, Response response) {
        super(application, request, response);
    }

    /**
     * Override to set up the conversation context and to choose the conversation
     * if a conversation id is present in target metadata.
     */
    @Override
    protected void onRequestTargetSet(IRequestTarget target) {
        super.onRequestTargetSet(target);

        Page page = null;
        if (target instanceof IPageRequestTarget) {
            page = ((IPageRequestTarget) target).getPage();
        }

        // Two possible specifications of cid: page metadata or request url; the
        // latter is used to propagate the conversation to mounted (bookmarkable)
        // paths after a redirect

        String cid = null;
        if (page != null) {
            cid = page.getMetaData(SeamMetaData.CID);
        } else {
            cid = request.getParameter("cid");
        }

        ConversationContext conversationContext = instance().select(HttpConversationContext.class).get();
        if (!conversationContext.isActive())
            conversationContext.activate(cid);
        Conversation conversation = conversationContext.getCurrentConversation();

        // handle propagation of existing long running converstaions to new
        // targets
        if (!conversation.isTransient()) {
            // Note that we can't propagate conversations with other redirect
            // targets like RequestRedirectTarget through this mechanism, because
            // it does not provide an interface to modify its target URL. If
            // propagation with those targets is to be supported, it needs a custom
            // Response subclass.
            if (isRedirect() && target instanceof BookmarkablePageRequestTarget) {
                BookmarkablePageRequestTarget bookmark = (BookmarkablePageRequestTarget) target;
                // if a cid has already been specified, don't override it
                if (!bookmark.getPageParameters().containsKey("cid"))
                    bookmark.getPageParameters().add("cid", conversation.getId());
            }

            // If we have a target page, propagate the conversation to the page's
            // metadata
            if (page != null) {
                page.setMetaData(SeamMetaData.CID, conversation.getId());
            }
        } else if (cid != null) {
            handleMissingConversation(cid);
        }

    }

    protected void handleMissingConversation(String id) {

    }

    @Override
    public void detach() {
        super.detach();
        ConversationContext conversationContext = instance().select(HttpConversationContext.class).get();
        try {
            conversationContext.invalidate();
        } catch (Exception e) {
        }
        try {
            conversationContext.deactivate();
        } catch (Exception e) {
        }
    }

    private static Instance<Context> instance() {
        return Container.instance().deploymentManager().instance().select(Context.class);
    }

}
