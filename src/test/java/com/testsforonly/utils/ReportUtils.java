package com.testsforonly.utils;

import org.testng.ITestResult;
import org.testng.Reporter;

public class ReportUtils {
  public static String getTestStatus(ITestResult result) {
    if (result.getStatus() == 1) {
      return "PASS";
    } else if (result.getStatus() == 2) {
      return "FAIL";
    }

    return "UNKNOWN";
  }

  public static void logf(String str, Object... arr) {
    String formattedMessage = String.format(str, arr);
    Reporter.log(formattedMessage, true);
  }
}
