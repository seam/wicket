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


import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.solder.logging.Logger;

/**
 * Request object producer which can be injected and control from test class.
 *
 * @author <a href="http://community.jboss.org/people/smigielski">Marek
 *         Smigielski</a>
 */
@ApplicationScoped
public class ConversationObjectProducer extends AbstractObjectProducer {

    @Inject
    Logger log;

    /**
     * Produces text message retrieving first element from localMessages. If local
     * message list is null or empty returning empty string.
     *
     * @return simple text message
     */
    @Produces
    @ConversationScopeQualifier
    @ConversationScoped
    public StringObject messageProducer() {
        StringObject stringObject = getMessage();
        log.info("Producing conversation object: " + stringObject.getValue());
        return stringObject;
    }

}
