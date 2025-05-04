package com.testsforonly.base;

import com.testsforonly.utils.ProjectUtils;
import com.testsforonly.utils.ReportUtils;
import io.github.bonigarcia.wdm.WebDriverManager;
import com.testsforonly.utils.DriverUtils;
import io.qameta.allure.Allure;
import org.openqa.selenium.SessionNotCreatedException;
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
import org.testng.annotations.Test;

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

    System.setProperty("webdriver.timeouts.implicitwait", "10000");
    System.setProperty("webdriver.timeouts.pageLoad", "30000");
    Reporter.log("INFO: Setup Webdriver manager", true);

    // Включаем подробное логирование
    System.setProperty("org.uncommons.reportng.escape-output", "false");
    System.setProperty("org.uncommons.reportng.title", "Test Reports");
    Reporter.setEscapeHtml(false);
    Reporter.log("INFO: Включаем подробное логирование", true);
  }

  @Parameters("browser")
  @BeforeMethod(alwaysRun = true)
  protected void setupDriver(@Optional("firefox") String browser, ITestContext context, ITestResult result) {
    final int maxAttempts = 3;
    int attempt = 0;
    WebDriver driver = null;

    String os = System.getProperty("os.label", "unknown");
    Allure.getLifecycle().updateTestCase(testResult -> {
      String oldName = testResult.getName(); // обычно имя метода
      String newName = oldName + " [" + os + " | " + browser + "]";
      testResult.setName(newName);
      testResult.setFullName(testResult.getFullName() + "_" + os + "_" + browser);
    });

    Allure.label("os", os);
    Allure.label("browser", browser);

    try {
      Reporter.log("BEFORE METHOD STARTED", true);
      Reporter.log("_________________________________________________________", true);
      Reporter.log("Run " + result.getMethod().getMethodName(), true);

      while (attempt < maxAttempts) {
        try {
          attempt++;
          Reporter.log("Attempt " + attempt + " to create " + browser + " driver", true);

          driver = DriverUtils.createDriver(browser);
          driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
          this.threadLocalDriver.set(driver);

          Reporter.log("DRIVER CREATED: " + (driver != null), true);
          break;
        } catch (SessionNotCreatedException e) {
          if (attempt == maxAttempts) {
            throw e;
          }
          Reporter.log("Attempt " + attempt + " failed. Retrying...", true);
          Thread.sleep(2000 * attempt);

          if (driver != null) {
            try { driver.quit(); } catch (Exception ignored) {}
          }
        }
      }

      if (driver == null) {
        String errorMsg = "Failed to create driver after " + maxAttempts + " attempts or unknown browser parameter" + browser;
        Reporter.log("ERROR: " + errorMsg, true);
        throw new IllegalArgumentException(errorMsg);
      } else {
        Reporter.log("INFO: " + browser.substring(0, 1).toUpperCase() + browser.substring(1) +
                " driver created (attempt " + attempt + ")", true);
      }

      Reporter.log("Test Thread ID: " + Thread.currentThread().getId(), true);
      Reporter.log("TEST SUITE: " + context.getCurrentXmlTest().getSuite().getName(), true);
      Reporter.log("RUN " + result.getMethod().getMethodName(), true);

    } catch (Throwable t) {
      System.err.println("ERROR IN @BEFORE METHOD (after " + attempt + " attempts):");
      t.printStackTrace();

      if (driver != null) {
        try { driver.quit(); } catch (Exception ignored) {}
      }
      threadLocalDriver.remove();

      try {
        throw t;
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @BeforeMethod
  public void beforeMethodDebug(ITestResult result) {
    System.out.println("TEST METHOD NAME: " + result.getMethod().getMethodName());
    System.out.println("TEST METHOD ENABLED: " + !result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).enabled());
  }

  @Parameters("browser")
  @AfterMethod(alwaysRun = true)
  protected void tearDown(@Optional("chrome") String browser, ITestResult result) {
    try {
      System.out.println("TEARDOWN STARTED");

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

    } catch (Throwable t) {
      System.err.println("ERROR IN @AFTER METHOD:");
      t.printStackTrace();
    }
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
