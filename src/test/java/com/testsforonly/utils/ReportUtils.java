package com.testsforonly.utils;

import org.testng.ITestResult;
import org.testng.Reporter;

public class ReportUtils {
  public static String getTestStatus(ITestResult result) {
    switch (result.getStatus()) {
      case ITestResult.SUCCESS: return "PASS";
      case ITestResult.FAILURE: return "FAIL";
      case ITestResult.SKIP: return "SKIP";
      default:
        logError("Unknown test status: " + result.getStatus());
        return "UNKNOWN";
    }
  }

  public static void logf(String str, Object... arr) {
    String message = String.format(str, arr);
    Reporter.log(message, true);
    System.out.println("REPORT: " + message);
  }

  private static void logError(String error) {
    System.err.println("ERROR: " + error);
    new Exception("Stack trace").printStackTrace();
  }
}
