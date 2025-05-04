package com.testsforonly.test;

import com.testsforonly.base.BaseTest;
import com.testsforonly.data.TestData;
import com.testsforonly.utils.OSLabelListener;
import com.testsforonly.utils.RetryAnalyzer;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.List;

@Listeners(OSLabelListener.class)
public class FooterTest extends BaseTest {

  @Test(
          groups = {"footer"},
          dataProviderClass = TestData.class,
          dataProvider = "pages",
          description = "TC-01.02 Check footer elements",
          testName = "Footer: Verify presence of all footer elements",
          retryAnalyzer = RetryAnalyzer.class
  )
  @Severity(SeverityLevel.NORMAL)
  @Story("Footer")
  @Description("Ensure that footer has all specified elements")

  public void testFooterElements(String url) {
    System.out.println("TEST BODY STARTED");

    Allure.step("Open " + url + " page");
    getDriver().get(url);

    Allure.step("Accepting cookies");
    try {
      WebElement okCookiesButton = getWait10().until(ExpectedConditions.presenceOfElementLocated(TestData.OKCOOKIEBUTTON));
      try {
        getWait10().until(ExpectedConditions.elementToBeClickable(okCookiesButton)).click();
        Reporter.log("Accept Cookies OK", true);
      } catch (Exception e) {
        Reporter.log("Try to click okcookie using JS", true);
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", okCookiesButton);
      }
    } catch (TimeoutException e) {
      Reporter.log("No cookie banner found - skipping", true);
    } catch (Exception e) {
      Reporter.log("Non-critical cookie handling error: " + e.getMessage(), true);
    }

    Allure.step("Scroll down to footer");
    try {

      WebElement footerElement = getWait10().until(ExpectedConditions.presenceOfElementLocated(TestData.FOOTER));

      ((JavascriptExecutor) getDriver()).executeScript("arguments[0].scrollIntoView({block: 'end'});", footerElement);

      getWait10().until(ExpectedConditions.visibilityOf(footerElement));

      Allure.step("Verify footer is displayed on " + url);
      Assert.assertTrue(footerElement.isDisplayed());

      Reporter.log("Footer is visible and scrolled into view on " + url, true);
      Allure.step("Footer is visible on: " + url);

    } catch (Exception e) {
      Reporter.log("Failed to scroll to " + url + " footer: " + e.getMessage(), true);
      Allure.step("Can't scroll to footer");
    }

    Allure.step("Choose footer option");
    List<By> footerElementsToCheck;
    if (url.equals(TestData.JOB_URL) || url.equals(TestData.CONTACTS_URL)) {
      footerElementsToCheck = TestData.SPECIAL_FOOTER_ELEMENTS;
    } else {
      footerElementsToCheck = TestData.DEFAULT_FOOTER_ELEMENTS;
    }

    Allure.step("Check visibility of element in footer");
    for (By locator : footerElementsToCheck) {
      Allure.step("Check visibility of footer element " + locator);
      Reporter.log("Check visibility of " + locator, true);

      WebElement element = getWait10().until(ExpectedConditions.visibilityOfElementLocated(locator));
      Assert.assertTrue(element.isDisplayed(), "Element not found: " + element);
    }
  }
}