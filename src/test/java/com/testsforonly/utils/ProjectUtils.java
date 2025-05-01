package com.testsforonly.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProjectUtils {
  private static final Properties properties = new Properties();

  static {
    try (InputStream input = ProjectUtils.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (input == null) {
        System.out.println("Sorry, unable to find config.properties");
      }
      properties.load(input);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
