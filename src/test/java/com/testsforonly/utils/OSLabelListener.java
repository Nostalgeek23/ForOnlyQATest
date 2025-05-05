package com.testsforonly.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Label;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class OSLabelListener implements ITestListener {

  @Override
  public void onTestStart(ITestResult result) {
    String osName = System.getProperty("os.name").toLowerCase();

    Allure.getLifecycle().updateTestCase(testResult -> {
      testResult.getLabels().add(new Label().setName("os").setValue(osName));
    });
  }
}
