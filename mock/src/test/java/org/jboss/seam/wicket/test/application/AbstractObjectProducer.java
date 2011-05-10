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

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract object producer which can be injected and control from test class.
 *
 * @author <a href="http://community.jboss.org/people/smigielski">Marek Smigielski</a>
 */
public class AbstractObjectProducer {

    protected List<String> localMessages = new LinkedList<String>();

    public AbstractObjectProducer() {
        super();
    }

    /**
     * Returning next message from the list
     *
     * @return StringObject wrapping text message
     */
    public StringObject getMessage() {
        if (localMessages.size() > 0) {
            String value = localMessages.remove(0);
            return new StringObject().setValue(value);
        }
        return new StringObject();
    }

    /**
     * Get local message list for manipulation. List implements LinkedList.
     *
     * @return local message list
     */
    public List<String> getLocalMessages() {
        return localMessages;
    }

    /**
     * Add single text message to the end of the list.
     *
     * @param text
     */
    public void add(String text) {
        localMessages.add(text);
    }

}
