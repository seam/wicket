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
package org.jboss.seam.wicket.test.core;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.wicket.SeamApplication;
import org.jboss.seam.wicket.mock.SeamWicketTester;
import org.jboss.seam.wicket.mock.SeamWicketTester.DummySeamApplication;
import org.jboss.seam.wicket.test.application.ConversationObjectProducer;
import org.jboss.seam.wicket.test.application.ConversationTestPage;
import org.jboss.seam.wicket.test.application.EmptyPage;
import org.jboss.seam.wicket.test.application.RequestObjectProducer;
import org.jboss.seam.wicket.test.application.RequestTestPage;
import org.jboss.seam.wicket.util.NonContextual;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Base test for wicket and seam integration.
 *
 * @author <a href="http://community.jboss.org/people/smigielski">Marek
 *         Smigielski</a>
 */
@RunWith(Arquillian.class)
public class SeamApplicationTest {
    @Deployment
    public static WebArchive createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(SeamApplication.class.getPackage())
                .addPackage(RequestTestPage.class.getPackage())
                .addPackage(NonContextual.class.getPackage())
                .addPackage(SeamWicketTester.class.getPackage())
                        // Page for testRenderPage
                .addAsResource("EmptyPage.html", "org/jboss/seam/wicket/test/application/EmptyPage.html")
                .addAsResource("EmptyPage.html", "org/jboss/seam/wicket/test/application/EmptyPageExpected.html")
                        // Page for testRequestScopeInjection
                .addAsResource("RequestTestPage.html", "org/jboss/seam/wicket/test/application/RequestTestPage.html")
                .addAsResource("RequestTestPageExpected.html", "org/jboss/seam/wicket/test/application/RequestTestPageExpected.html")
                        // Page for testConversationScopeInjection
                .addAsResource("ConversationTestPage.html", "org/jboss/seam/wicket/test/application/ConversationTestPage.html")
                .addAsResource("ConversationTestPageExpected.html", "org/jboss/seam/wicket/test/application/ConversationTestPageExpected.html")

                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(
                        DependencyResolvers.use(MavenDependencyResolver.class)
                        .configureFrom("../settings.xml")
                        .loadMetadataFromPom("pom.xml")
                        .artifact("org.jboss.solder:solder-impl")
                        .artifact("org.apache.wicket:wicket").exclusion("org.slf4j:slf4j-api")
                        .artifact("org.slf4j:slf4j-simple")
                        .resolveAs(JavaArchive.class))
                .setWebXML("test-web.xml");
    }

    @Inject
    SeamWicketTester tester;

    @Inject
    RequestObjectProducer requestObjectProducer;

    @Inject
    ConversationObjectProducer conversationObjectProducer;

    /**
     * Test that simple rendering works fine and we get what we have expected.
     *
     * @throws Exception
     */
    @Test
    public void testRenderPage() throws Exception {
        Assert.assertNotNull(tester);
        tester.startPage(EmptyPage.class);

        tester.assertRenderedPage(EmptyPage.class);

        tester.assertResultPage(EmptyPage.class, "EmptyPageExpected.html");
    }

    /**
     * Test that page injection of request object works both in and out.
     *
     * @throws Exception
     */
    @Test
    public void testRequestScopeInjection() throws Exception {
        Assert.assertNotNull(tester);
        // initialize request object values
        requestObjectProducer.add("First request message");
        requestObjectProducer.add("Second request message");
        requestObjectProducer.add("This message is not retrived");

        // test first request
        tester.startPage(RequestTestPage.class);
        tester.assertRenderedPage(RequestTestPage.class);
        tester.assertResultPage(RequestTestPage.class, "RequestTestPageExpected.html");

        // test second request
        tester.clickLink("refresh");
        tester.assertRenderedPage(RequestTestPage.class);
        tester.assertLabel("headerLabel", "First request message");
        tester.assertLabel("spanLabel", "Second request message");
        tester.assertLabel("refresh:number", "1");
    }

    /**
     * Test that page injection of request object works both in and out.
     *
     * @throws Exception
     */
    @Test
    public void testConversationScopeInjection() throws Exception {
        Assert.assertNotNull(tester);
        DummySeamApplication application = (DummySeamApplication) tester.getApplication();
        application.setManuallyDetach(true);
        // initialize conversation object values
        conversationObjectProducer.add("First conversation message");
        conversationObjectProducer.add("Second conversation message");
        conversationObjectProducer.add("This message is not retrived");

        // test first request in first conversation
        tester.startPage(ConversationTestPage.class);
        tester.assertRenderedPage(ConversationTestPage.class);
        tester.assertResultPage(ConversationTestPage.class, "ConversationTestPageExpected.html");
        // always detach request after all tests on current page
        application.detach();
        // test second request in first conversation
        tester.clickLink("refresh");

        tester.assertRenderedPage(ConversationTestPage.class);
        // without manually detaching the conversation scope is closed to early.
        tester.assertLabel("headerLabel", "First conversation message");
        tester.assertLabel("spanLabel", "First conversation message");
        tester.assertLabel("refresh:number", "1");
        // always detach request after all tests on current page
        application.detach();

        // test first request in second conversation
        tester.startPage(ConversationTestPage.class);
        tester.assertRenderedPage(ConversationTestPage.class);
        tester.assertLabel("headerLabel", "Second conversation message");
        tester.assertLabel("spanLabel", "Second conversation message");
        tester.assertLabel("refresh:number", "0");
        // always detach request after all tests on current page
        application.detach();

        // test second request in second conversation
        tester.clickLink("refresh");
        tester.assertRenderedPage(ConversationTestPage.class);
        tester.assertLabel("headerLabel", "Second conversation message");
        tester.assertLabel("spanLabel", "Second conversation message");
        tester.assertLabel("refresh:number", "1");
        // always detach request after all tests on current page
        application.detach();
    }
}
