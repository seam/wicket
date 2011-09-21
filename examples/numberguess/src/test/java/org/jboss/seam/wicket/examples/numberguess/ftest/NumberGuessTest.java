package org.jboss.seam.wicket.examples.numberguess.ftest;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;



/**
 * Tests for the numberguess example
 *
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 */
@RunWith(Arquillian.class)
public class NumberGuessTest{
    private HomePage page;
    public static final String ARCHIVE_NAME = "wicket-numberguess.war";
    public static final String BUILD_DIRECTORY = "target";
    
    @ArquillianResource
    URL contextPath;
    
    @Drone
    AjaxSelenium selenium;
    
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(ZipImporter.class, ARCHIVE_NAME).importFrom(new File(BUILD_DIRECTORY + '/' + ARCHIVE_NAME))
                .as(WebArchive.class);
    }

    @Before
    public void preparePage() {
        page = new HomePage(selenium, contextPath);
    }

    @Test
    public void smartTest() {
        int guess;
        int i = 0;

        assertTrue(!page.isLost() && !page.isWon()); // initial state check

        while (page.canGuess()) {
            if (page.isLost() || page.isWon()) {
                fail();
            }
            if (i > 9) {
                fail("Game should not be longer than 10 guesses");
            }
            guess = page.getMin() + ((page.getMax() - page.getMin()) / 2);
            page.guess(guess);
            i++;
        }

        assertTrue("The computer is supposed to win this game.",page.isWon());
    }

    @Test
    public void linearTest() {
        int guess = 0;

        assertTrue(!page.isLost() && !page.isWon()); // initial state check

        while (page.canGuess()) {
            page.guess(guess++);
            assertTrue("Guess count exceeded.",guess <= 10);
        }

        if (guess < 10) {
            assertTrue("Player should not lose before 10th guess.",page.isWon());
        } else {
            assertTrue("The game must be either lost or won after 10 attempts.",page.isWon() || page.isLost());
        }
    }

    @Test
    public void resetTest() {
        page.guess(50);

        // one out of 100 attempts wins instantly, there is nothing we can do about it

        if (page.canGuess()) {
            page.reset();

            assertEquals("Reset did not work.",page.getMin(), 0);
            assertEquals( "Reset did not work.",page.getMax(), 100);
            assertEquals("Reset did not work.",page.getGuessLeft(), 10);
        }
    }
}
