package org.jboss.seam.wicket.example.numberguess.ftest;

import org.jboss.test.selenium.AbstractTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Tests for the numberguess example
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 *
 */
public class NumberGuessTest extends AbstractTestCase
{
   private HomePage page;

   @BeforeMethod
   public void preparePage()
   {
      page = new HomePage(selenium, contextPath);
   }

   @Test
   public void smartTest()
   {
      int guess;
      int i = 0;
      
      assertTrue(!page.isLost() && !page.isWon()); // initial state check

      while (page.canGuess())
      {
         if (page.isLost() || page.isWon())
         {
            fail();
         }
         if (i > 9)
         {
            fail("Game should not be longer than 10 guesses");
         }
         guess = page.getMin() + ((page.getMax() - page.getMin()) / 2);
         page.guess(guess);
         i++;
      }
      
      assertTrue(page.isWon(), "The computer is supposed to win this game.");
   }

   @Test
   public void linearTest()
   {
      int guess = 0;
      
      assertTrue(!page.isLost() && !page.isWon()); // initial state check

      while (page.canGuess())
      {
         page.guess(guess++);
         assertTrue(guess <= 10, "Guess count exceeded.");
      }
      
      if (guess < 10)
      {
         assertTrue(page.isWon(), "Player should not lose before 10th guess.");
      }
      else
      {
         assertTrue(page.isWon() || page.isLost(), "The game must be either lost or won after 10 attempts.");
      }
   }
   
   @Test
   public void resetTest()
   {
      page.guess(50);
      
      // one out of 100 attempts wins instantly, there is nothing we can do about it
      
      if (page.canGuess())
      {
         page.reset();
         
         assertEquals(page.getMin(), 0, "Reset did not work.");
         assertEquals(page.getMax(), 100, "Reset did not work.");
         assertEquals(page.getGuessLeft(), 10, "Reset did not work.");
      }
   }
}
