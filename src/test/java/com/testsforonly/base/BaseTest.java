package com.testsforonly.base;

import com.testsforonly.utils.ProjectUtils;
import com.testsforonly.utils.ReportUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.testsforonly.utils.DriverUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.time.Duration;

public abstract class BaseTest {
  private final ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
  private final ThreadLocal<WebDriverWait> wait10 = new ThreadLocal<>();

  @BeforeSuite(alwaysRun = true)
  protected void setupWebDriverManager() {
    WebDriverManager.chromedriver().setup();
    WebDriverManager.firefoxdriver().setup();
    WebDriverManager.edgedriver().setup();
    WebDriverManager.safaridriver().setup();

    Reporter.log("INFO: Setup Webdriver manager", true);
  }

  @Parameters("browser")
  @BeforeMethod(alwaysRun = true)
  protected void setupDriver(@Optional("chrome") String browser, ITestContext context, ITestResult result) {
    Reporter.log("_________________________________________________________", true);
    Reporter.log("Run " + result.getMethod().getMethodName(), true);

    WebDriver driver = DriverUtils.createDriver(browser);
    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
    this.threadLocalDriver.set(driver);

    Reporter.log("Test Thread ID: " + Thread.currentThread().getId(), true);
    Reporter.log("TEST SUITE: " + context.getCurrentXmlTest().getSuite().getName(), true);
    Reporter.log("RUN " + result.getMethod().getMethodName(), true);

    if (driver == null) {
      Reporter.log("ERROR: unknown browser parameter" + browser, true);
      throw new IllegalArgumentException("Unknown 'browser' parameter - " + browser);
    } else {
      Reporter.log("INFO: " + browser.substring(0, 1).toUpperCase() + browser.substring(1) +
              " driver created", true);
    }
  }

  @Parameters("browser")
  @AfterMethod(alwaysRun = true)
  protected void tearDown(@Optional("chrome") String browser, ITestResult result) {
    WebDriver driver = getDriver();

    Reporter.log("INFO: " + result.getMethod().getMethodName() + ": " + ReportUtils.getTestStatus(result),
            true);

    if (driver != null) {
      try {
        driver.quit();
        Reporter.log("INFO: " + browser.substring(0, 1).toUpperCase() + browser.substring(1) +
                " driver closed", true);
      } finally {
        Reporter.log("After Test Thread ID: " + Thread.currentThread().getId(), true);

        threadLocalDriver.remove();
        wait10.remove();
      }
    } else {
      Reporter.log("INFO: Driver is null", true);
    }

    ReportUtils.logf("Execution time is %d sec\n", (result.getEndMillis() - result.getStartMillis()) / 1000);
  }

  @AfterClass(alwaysRun = true)
  public void tearDownClass() {
    WebDriver driver = getDriver();

    if (driver != null) {
      try {
        driver.quit();
      } finally {
        threadLocalDriver.remove();
        wait10.remove();
      }
    }
  }

  protected WebDriver getDriver() {
    return threadLocalDriver.get();
  }

  protected WebDriverWait getWait10() {
    if (wait10.get() == null) {
      wait10.set(new WebDriverWait(getDriver(), Duration.ofSeconds(10)));
    }
    return wait10.get();
  }
}
