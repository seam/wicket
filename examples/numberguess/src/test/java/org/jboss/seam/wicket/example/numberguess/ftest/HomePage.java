package org.jboss.seam.wicket.example.numberguess.ftest;

import static org.jboss.test.selenium.locator.LocatorFactory.jq;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.locator.JQueryLocator;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.waitXhr;

public class HomePage
{
   public static final JQueryLocator BUTTON_GUESS = jq("[name='GuessButton']");
   public static final JQueryLocator BUTTON_RESET = jq("[name='RestartButton']");
   public static final JQueryLocator FIELD_INPUT = jq("[name='inputGuess']");
   public static final JQueryLocator PROMPT = jq("#prompt");
   public static final Pattern PROMPT_PATTERN = Pattern.compile("I'm thinking of a number between (\\d+) and (\\d+). You have (\\d+) guesses.");
   
   private AjaxSelenium selenium;
   
   public HomePage(AjaxSelenium selenium, URL contextPath)
   {
      this.selenium = selenium;
      selenium.open(contextPath);
      selenium.waitForPageToLoad();
      reset();
   }
   
   public void guess(Integer guess)
   {
      selenium.type(FIELD_INPUT, guess.toString());
      waitXhr(selenium).click(BUTTON_GUESS);
   }
   
   public void reset()
   {
      waitXhr(selenium).click(BUTTON_RESET);
   }
   
   public int getMin()
   {
      String prompt = selenium.getText(PROMPT);
      return getValue(prompt, 1);
   }
   
   public int getMax()
   {
      String prompt = selenium.getText(PROMPT);
      return getValue(prompt, 2);
   }
   
   public int getGuessLeft()
   {
      String prompt = selenium.getText(PROMPT);
      return getValue(prompt, 3);
   }
   
   public boolean isWon()
   {
      return selenium.isTextPresent("Correct!");
   }
   
   public boolean isLost()
   {
      return selenium.isTextPresent("Sorry, the answer was");
   }
   
   public boolean canGuess()
   {
      return selenium.isElementPresent(BUTTON_GUESS);
   }
   
   static int getValue(String prompt, int group)
   {
      Matcher matcher = PROMPT_PATTERN.matcher(prompt);
      if (matcher.find())
      {
         return Integer.parseInt(matcher.group(group));
      }
      else
      {
         throw new RuntimeException("Unable to find group " + group + " in the following prompt: " + prompt);
      }
   }
}
