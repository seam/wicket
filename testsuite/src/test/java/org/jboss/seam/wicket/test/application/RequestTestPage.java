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
package org.jboss.seam.wicket.test.application;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.value.Count;
import org.jboss.solder.logging.Logger;

/**
 * Simple wicket test page for request object injection
 *
 * @author <a href="http://community.jboss.org/people/smigielski">Marek
 *         Smigielski</a>
 */
public class RequestTestPage extends WebPage {
    @Inject
    Logger log;

    @Inject
    @RequestScopeQualifier
    StringObject stringObject;

    @Inject
    @RequestScopeQualifier
    Instance<StringObject> stringObjectInstance;

    public RequestTestPage() {
        log.info("Rendering test page");
        // headerLabel is CDI object cached in wicket
        Label headerLabel = new Label("headerLabel", new Model<String>(stringObject.getValue()));
        headerLabel.setEscapeModelStrings(false);
        add(headerLabel);

        // spanLabel is CDI object retrieved dynamically every request
        Label spanLabel = new Label("spanLabel", new Model<String>() {
            @Override
            public String getObject() {
                return stringObjectInstance.get().getValue();
            }
        });
        spanLabel.setEscapeModelStrings(false);
        add(spanLabel);

        // Counter is wicket managed object.
        final Count count = new Count(); // simple counter object
        Link refresh = new Link("refresh") {
            public void onClick() {
                count.increment();
            }
        };
        refresh.add(new Label("number", new Model<String>() {
            public String getObject() {
                return Integer.toString(count.getCount());
            }
        }));
        add(refresh);

    }
}
