package APITestingRestAssured;

import cucumber.api.junit.Cucumber;
import cucumber.api.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/java/featuresFiles",
        glue = {"APITestingRestAssured/stepDefinitions"},
        plugin = { "pretty", "json:target/cucumber-reports/Cucumber.json",
                   "junit:target/cucumber-reports/Cucumber.xml",
                   "html:target/cucumber-reports/html"},
        monochrome = true,
        tags = {"@RegressionTest"}
)

public class RunAPITest {
   // Runner


}
