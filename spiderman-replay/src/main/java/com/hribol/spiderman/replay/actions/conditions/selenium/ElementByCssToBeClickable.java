package com.hribol.spiderman.replay.actions.conditions.selenium;

import com.hribol.spiderman.replay.actions.WebDriverAction;
import com.hribol.spiderman.replay.filters.ReplayFiltersFacade;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by hvrigazov on 18.05.17.
 */
public class ElementByCssToBeClickable implements WebDriverAction {

    private final String cssSelector;
    private final String eventName;
    private final Integer timeout;

    public ElementByCssToBeClickable(String cssSelector, String eventName, Integer timeout) {
        this.cssSelector = cssSelector;
        this.eventName = eventName;
        this.timeout = timeout;
    }

    @Override
    public void execute(WebDriver driver, ReplayFiltersFacade facade) {
        By by = By.cssSelector(cssSelector);
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    @Override
    public String getName() {
        return eventName;
    }

    @Override
    public boolean expectsHttpRequest() {
        return false;
    }
}
