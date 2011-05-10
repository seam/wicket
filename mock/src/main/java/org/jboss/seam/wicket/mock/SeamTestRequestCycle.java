/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.wicket.mock;

import org.apache.wicket.Response;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebRequest;
import org.jboss.seam.wicket.SeamRequestCycle;

/**
 * Simple request cycle with manual control of detaching conversation context and rest of wicket stuff.
 *
 * @author <a href="http://community.jboss.org/people/smigielski">Marek
 *         Smigielski</a>
 */
public class SeamTestRequestCycle extends SeamRequestCycle {
    private boolean detach;


    public SeamTestRequestCycle(WebApplication application, WebRequest request, Response response, boolean detach) {
        super(application, request, response);
        this.detach = detach;
    }

    /**
     * Detach only when you know that conversation scope isn't need any more.
     *
     * @see org.jboss.seam.wicket.SeamRequestCycle#detach()
     */
    @Override
    public void detach() {
        if (isDetach()) {
            super.detach();
        }
    }

    public void superDetach() {
        super.detach();
    }


    public boolean isDetach() {
        return detach;
    }

    public void setDetach(boolean detach) {
        this.detach = detach;
    }
}
