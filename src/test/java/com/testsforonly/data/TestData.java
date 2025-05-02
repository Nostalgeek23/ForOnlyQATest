package com.testsforonly.data;

import org.openqa.selenium.By;
import org.testng.annotations.DataProvider;

import java.util.List;

public class TestData {
  public static final String BASE_URL = "https://only.digital";

  public static final String PROJECTS_URL = BASE_URL + "/projects";
  public static final String CONTACTS_URL = BASE_URL + "/contacts";
  public static final String COMPANY_URL = BASE_URL + "/company";
  public static final String FIELDS_URL = BASE_URL + "/fields";
  public static final String BLOG_URL = BASE_URL + "/blog";
  public static final String JOB_URL = BASE_URL + "/job";

  public static final By FOOTER = By.cssSelector("footer[class^='Footer']");
  public static final By COPYRIGHTS = By.cssSelector("footer > div[class*='copyrights']");
  public static final By STARTPROJECTBUTTON = By.cssSelector("footer button[class*='StartProject']");
  public static final By COMPANYLOGO = By.cssSelector("footer svg[class*='logo']");
  public static final By BEHANCEBUTTON = By.cssSelector("footer a[href*='behance']");
  public static final By DPROFILEBUTTON = By.cssSelector("footer a[href*='dprofile']");
  public static final By TELEGRAMBUTTON = By.cssSelector("footer a[href*='t.me']");
  public static final By VKBUTTON = By.cssSelector("footer a[href*='vk.com']");
  public static final By TELEGRAMCONTACT = By.cssSelector("footer div[class^='Telegram']");
  public static final By CONTACTSLINKS = By.cssSelector("footer div[class^='ContactsLinks']");
  public static final By FOOTERTEXT = By.cssSelector("footer p[class^='text2']");
  public static final By OKCOOKIEBUTTON = By.cssSelector("button[class*='Cookie']");

  public static final List<By> DEFAULT_FOOTER_ELEMENTS = List.of(
          COMPANYLOGO,
          STARTPROJECTBUTTON,
          TELEGRAMCONTACT,
          CONTACTSLINKS,
          FOOTERTEXT,
          COPYRIGHTS,
          BEHANCEBUTTON,
          DPROFILEBUTTON,
          TELEGRAMBUTTON,
          VKBUTTON
  );

  public static final List<By> SPECIAL_FOOTER_ELEMENTS = List.of(
          COPYRIGHTS, BEHANCEBUTTON, DPROFILEBUTTON, TELEGRAMBUTTON, VKBUTTON
  );

  @DataProvider(name = "pages")
  public static Object[][] pagesUrls() {
    return new Object[][]{
            {BASE_URL},
            {PROJECTS_URL},
            {CONTACTS_URL},
            {COMPANY_URL},
            {FIELDS_URL},
            {BLOG_URL},
            {JOB_URL}
    };
  }

}
