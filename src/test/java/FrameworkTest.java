import com.testsforonly.base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Link;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

public class FrameworkTest extends BaseTest {

  @Test(
          groups = {"smoke"},
          description = "TC-00.00 AssertTrue = true",
          testName = "AssertTrue"
  )
  @Severity(SeverityLevel.BLOCKER)
  @Story("Smoke")
  @Description("TC-00.00 AssertTrue = true")
  public void testPass() {
    Assert.assertTrue(true);
  }

  @Ignore
  @Test
  public void testFail() {
    Assert.assertTrue(false);
  }

  @Test(
          groups = {"smoke"},
          description = "TC-00.01 Open Google.com",
          testName = "Navigation: Open Google"
  )
  @Severity(SeverityLevel.BLOCKER)
  @Story("Smoke")
  @Description("TC-00.01 Open Google.com")
  @Link("https://www.google.com/")
  public void testGoogle() {
    final String expectedURL = "https://www.google.com/";

    getDriver().get(expectedURL);

    final String actualURL = getDriver().getCurrentUrl();

    Assert.assertEquals(actualURL, expectedURL);
  }

  @Test(
          groups = {"smoke"},
          description = "TC-00.01 Open Google.com",
          testName = "Navigation: Open Google"
  )
  @Severity(SeverityLevel.BLOCKER)
  @Story("Smoke")
  @Description("TC-00.01 Open Google.com")
  @Link("https://github.com/")
  public void testGitHub() {
    final String expectedURL = "https://github.com/";

    getDriver().get(expectedURL);

    final String actualURL = getDriver().getCurrentUrl();

    Assert.assertEquals(actualURL, expectedURL);
  }
}
