package org.jboss.seam.wicket.examples.numberguess.ftest;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.utils.URLUtils;
import org.jboss.arquillian.drone.api.annotation.Drone;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.jboss.arquillian.ajocado.Ajocado.waitForXhr;

public class HomePage {
    public static final JQueryLocator BUTTON_GUESS = jq("[name='GuessButton']");
    public static final JQueryLocator BUTTON_RESET = jq("[name='RestartButton']");
    public static final JQueryLocator FIELD_INPUT = jq("[name='inputGuess']");
    public static final JQueryLocator PROMPT = jq("#prompt");
    public static final Pattern PROMPT_PATTERN = Pattern.compile("I'm thinking of a number between (\\d+) and (\\d+). You have (\\d+) guesses.");
    public static final String HOME_PAGE = "/wicket-numberguess";

    @Drone
    private AjaxSelenium selenium;

    public HomePage(AjaxSelenium selenium, URL contextPath) {
        this.selenium = selenium;
        selenium.open(URLUtils.buildUrl(contextPath, HOME_PAGE));
        selenium.waitForPageToLoad();
        reset();
    }

    public void guess(Integer guess) {
        selenium.type(FIELD_INPUT, guess.toString());
        waitForXhr(selenium).click(BUTTON_GUESS);
    }

    public void reset() {
        waitForXhr(selenium).click(BUTTON_RESET);
    }

    public int getMin() {
        String prompt = selenium.getText(PROMPT);
        return getValue(prompt, 1);
    }

    public int getMax() {
        String prompt = selenium.getText(PROMPT);
        return getValue(prompt, 2);
    }

    public int getGuessLeft() {
        String prompt = selenium.getText(PROMPT);
        return getValue(prompt, 3);
    }

    public boolean isWon() {
        return selenium.isTextPresent("Correct!");
    }

    public boolean isLost() {
        return selenium.isTextPresent("Sorry, the answer was");
    }

    public boolean canGuess() {
        return selenium.isElementPresent(BUTTON_GUESS);
    }

    static int getValue(String prompt, int group) {
        Matcher matcher = PROMPT_PATTERN.matcher(prompt);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(group));
        } else {
            throw new RuntimeException("Unable to find group " + group + " in the following prompt: " + prompt);
        }
    }
}
